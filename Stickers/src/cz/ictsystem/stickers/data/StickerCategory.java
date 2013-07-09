package cz.ictsystem.stickers.data;

import android.content.ContentValues;
import android.content.Context;

import com.google.gdata.data.spreadsheet.ListEntry;

import cz.ictsystem.stickers.R;

public class StickerCategory {
	int mId;
	int mCategoryId;
	int mStickerId;
	
	public StickerCategory(int id, int categoryId, int stickerId){
		mId = id;
		mCategoryId = categoryId;
		mStickerId = stickerId;
	}

	public StickerCategory(Context context, ListEntry entry){
		mId = Integer.valueOf(entry.getCustomElements().getValue("id"));
		mCategoryId = Integer.valueOf(entry.getCustomElements().getValue(context.getString(R.string.column_stickercategory_category_id)));
		mStickerId = Integer.valueOf(entry.getCustomElements().getValue(context.getString(R.string.column_stickercategory_sticker_id)));
	}

	public int getId() {
		return mId;
	}

	public int getCategoryId() {
		return mCategoryId;
	}

	public int getStickerId() {
		return mStickerId;
	}

	public ContentValues getStickerCategory(Context context){
		final ContentValues contentValues = new ContentValues();
		contentValues.put(context.getString(R.string.column_id), mId);
		contentValues.put(context.getString(R.string.column_stickercategory_category_id), mCategoryId);
		contentValues.put(context.getString(R.string.column_stickercategory_sticker_id), mStickerId);
		return contentValues;
	}
}
