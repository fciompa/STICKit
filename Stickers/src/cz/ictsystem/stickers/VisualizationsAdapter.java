package cz.ictsystem.stickers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.LightingColorFilter;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import cz.ictsystem.lib.GallerySendOrderBackground;
import cz.ictsystem.lib.GalleryDateBackground;
import cz.ictsystem.lib.GalleryTitleBackground;
import cz.ictsystem.lib.ImageViewAsyn;
import cz.ictsystem.stickers.data.DbProvider;


public class VisualizationsAdapter extends SimpleCursorAdapter{

	public VisualizationsAdapter(Context context, Cursor cursor) {
		super(context,
				R.layout.visualization_item,
				cursor, 
				new String[] {context.getString(R.string.column_id)},
				new int[] {R.string.column_id}, 
				FLAG_REGISTER_CONTENT_OBSERVER);

	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
    	ItemHolder holder = (ItemHolder) view.getTag();
    	
    	if(holder == null){
            holder = new ItemHolder(context, view);
            view.setTag(holder);
    	}
    	holder.populate(cursor);
	}

    private class ItemHolder {
		private Context mContext;
		private int mId;
    	private ImageViewAsyn mImageAsyn;
		private TextView mUpdateDate;
		private TextView mName;
		private ImageButton mSendOrder;

		@SuppressWarnings("deprecation")
		ItemHolder(Context context, View item) {
    		mContext = context;
    		ImageView imageView = (ImageView) item.findViewById(R.id.visualization_image);
    		imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					View parentView = (View) view.getParent();
					ItemHolder holder = (ItemHolder) parentView.getTag();

				    	if(holder.mId != 10){
				    		Intent intent = new Intent(mContext.getApplicationContext(), ActivityVisualizationDetail.class);
				    		intent.putExtra(Const.ARG_ID, Long.valueOf(holder.mId).intValue());
				    		mContext.startActivity(intent);
				    	} else {
				    		//Visualization with id == 10 is only motivation picture, so it is add new visualization
				    		((ActivityMain)mContext).onNewVisualization();
				    	}
				}
			});
    		Point display = new Point(Utils.getDisplaySize(mContext).x/2, Utils.getDisplaySize(mContext).y/2);
    		mImageAsyn = new ImageViewAsyn(Utils.mVisualizationImageCache, display, imageView);
    		mUpdateDate = (TextView) item.findViewById(R.id.visualization_update_date);
    		mUpdateDate.setBackgroundDrawable((new GalleryDateBackground(
    				context, 
    				mContext.getResources().getColor(R.color.st_galery_date_background),
    				mContext.getResources().getColor(R.color.st_galery_title_background))).get());

    		mName = (TextView) item.findViewById(R.id.visualization_title);
    		mName.setBackgroundDrawable((new GalleryTitleBackground(
    				context, 
    				mContext.getResources().getColor(R.color.st_galery_title_background))).get());
    		
    		mSendOrder = (ImageButton) item.findViewById(R.id.visualization_send_order);
    		mSendOrder.setBackgroundDrawable((new GallerySendOrderBackground(
    				context, 
    				mContext.getResources().getColor(R.color.st_galery_title_background))).get());
    		
    		Drawable drawable = mSendOrder.getDrawable();
    		drawable.setColorFilter(new LightingColorFilter(0x000000, 0xffffff));
    		
    		mSendOrder.setImageDrawable(drawable);
    		mSendOrder.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					View parentView = (View) view.getParent();
					ItemHolder holder = (ItemHolder) parentView.getTag();
					if(holder.mId != 10){
						//Visualization with id == 10 is only motivation picture, so ...
						((ActivityMain)mContext).onSendOrder(Const.MAIL_TYPE_ORDER, holder.mId);
					}
				}
    		});
	    }
		
		@SuppressLint("SimpleDateFormat")
		void populate(Cursor cursor){
			mId = cursor.getInt(cursor.getColumnIndex(mContext.getString(R.string.column_id)));
			String updateDate = cursor.getString(cursor.getColumnIndex(mContext.getString(R.string.column_visualization_update_date)));
			String name = cursor.getString(cursor.getColumnIndex(mContext.getString(Utils.isCZ() ? R.string.column_visualization_name_cz : R.string.column_visualization_name_eng)));
			byte[] image = cursor.getBlob(cursor.getColumnIndex(mContext.getString(R.string.column_visualization_image)));

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			try {
				mUpdateDate.setText(DateFormat.getDateInstance().format(dateFormat.parse(updateDate)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			mName.setText(name);
			mImageAsyn.load(mId, image);
			Log.d("error", "populate end");
		}
	}
}
