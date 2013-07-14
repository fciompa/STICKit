package cz.ictsystem.stickers.data;

import java.util.HashMap;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import cz.ictsystem.stickers.R;

public class Category {
	private int mId;
	private String mNameCZ;
	private String mNameENG;
	private int mSequence;
	
	public Category(int id, String nameCZ, String nameENG, int sequence){
		mId = id;
		mNameCZ = nameCZ;
		mNameENG = nameENG;
		mSequence = sequence;
	}
	
	@SuppressLint("DefaultLocale")
	public Category(Context context, HashMap<String, String> elements){
		mId = Integer.valueOf(elements.get(context.getString(R.string.column_id).toLowerCase(Locale.US)));
		mNameCZ = elements.get(context.getString(R.string.column_category_name_cz).toLowerCase(Locale.US));
		mNameENG = elements.get(context.getString(R.string.column_category_name_eng).toLowerCase(Locale.US));
		mSequence = Integer.valueOf(elements.get(context.getString(R.string.column_category_sequence).toLowerCase(Locale.US)));
	}

	public Category(Cursor cursor){
		mId = cursor.getInt(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_category, R.string.column_id)));
		mNameCZ = cursor.getString(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_category, R.string.column_category_name_cz)));
		mNameENG = cursor.getString(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_category, R.string.column_category_name_eng)));
		mSequence = cursor.getInt(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_category, R.string.column_category_sequence)));
	}
	
	public int getId(){
		return mId;
	}
	
	public String getNameCZ(){
		return mNameCZ;
	}
	
	public String getNameENG(){
		return mNameENG;
	}
	
	public int getSequence(){
		return mSequence;
	}
	public ContentValues getCategory(Context context){
		final ContentValues contentValues = new ContentValues();
		contentValues.put(context.getString(R.string.column_id), mId);
		contentValues.put(context.getString(R.string.column_category_name_cz), mNameCZ);
		contentValues.put(context.getString(R.string.column_category_name_eng), mNameENG);
		contentValues.put(context.getString(R.string.column_category_sequence), mSequence);
		return contentValues;
	}
}
