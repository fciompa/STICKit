package cz.ictsystem.stickers.data;

import java.util.HashMap;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
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

	public StickerCategory(Context context, HashMap<String, String> elements){
		mId = Integer.valueOf(elements.get(context.getString(R.string.column_id).toLowerCase(Locale.US)));
		mCategoryId = Integer.valueOf(elements.get(context.getString(R.string.column_stickercategory_category_id).toLowerCase(Locale.US)));
		mStickerId = Integer.valueOf(elements.get(context.getString(R.string.column_stickercategory_sticker_id).toLowerCase(Locale.US)));
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
