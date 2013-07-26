package cz.ictsystem.stickers;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Point;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cz.ictsystem.lib.GalleryTitleBackground;
import cz.ictsystem.lib.ImageViewAsyn;
import cz.ictsystem.stickers.data.Sticker;

public class StickersAdapter extends SimpleCursorAdapter{
	
	Point mDisplaySize;

	public StickersAdapter(Context context, Cursor cursor) {
		super(context,
				R.layout.sticker_item,
				cursor, 
				new String[] {
					context.getString(R.string.column_id), 
					context.getString(Utils.isCZ() ? R.string.column_sticker_name_cz : R.string.column_sticker_name_eng), 
					context.getString(R.string.column_sticker_image)},
				new int[] {
					R.string.column_id,
					Utils.isCZ() ? R.string.column_sticker_name_cz : R.string.column_sticker_name_eng,
					R.string.column_sticker_image}, 
				FLAG_REGISTER_CONTENT_OBSERVER);
		mDisplaySize = Utils.getDisplaySize(context);
		
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
		private ImageViewAsyn mImage; 
		private TextView mName;
		
		@SuppressWarnings("deprecation")
		ItemHolder(Context context, View item) {
    		mImage = new ImageViewAsyn(Utils.mStickerImageCache, mDisplaySize, (ImageView) item.findViewById(R.id.sticker_image));
    		mName = (TextView) item.findViewById(R.id.sticker_name);
//    		mName.setBackground((new GalleryTitleBackground(context, context.getResources().getColor(R.color.st_galery_title_background))).get());
    		mName.setBackgroundDrawable((new GalleryTitleBackground(context, context.getResources().getColor(R.color.st_galery_title_background))).get());    		
	    }
		
		void populate(Cursor cursor) {
			Sticker sticker = new Sticker(cursor);
			mName.setText(sticker.getName());
			mImage.load(sticker.getId(), sticker.getImage());
			sticker = null;
		}
    }
}
