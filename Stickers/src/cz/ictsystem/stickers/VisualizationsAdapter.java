package cz.ictsystem.stickers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Point;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cz.ictsystem.lib.GalleryDateBackground;
import cz.ictsystem.lib.GalleryTitleBackground;
import cz.ictsystem.lib.ImageViewAsyn;


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
    	private ImageViewAsyn mImageAsyn;
		private TextView mUpdateDate;
		private TextView mName;

		@SuppressWarnings("deprecation")
		ItemHolder(Context context, View item) {
    		mContext = context;
    		ImageView imageView = (ImageView) item.findViewById(R.id.visualization_image);
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
	    }
		
		@SuppressLint("SimpleDateFormat")
		void populate(Cursor cursor){
			int id = cursor.getInt(cursor.getColumnIndex(mContext.getString(R.string.column_id)));
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
			mImageAsyn.load(id, image);
			Log.d("error", "populate end");
		}
	}
}
