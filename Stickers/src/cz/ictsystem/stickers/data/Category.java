package cz.ictsystem.stickers.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.gdata.data.spreadsheet.ListEntry;

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
	
	public Category(Context context, ListEntry entry){
		mId = Integer.valueOf(entry.getCustomElements().getValue("id"));
		mNameCZ = entry.getCustomElements().getValue(context.getString(R.string.column_category_name_cz));
		mNameENG = entry.getCustomElements().getValue(context.getString(R.string.column_category_name_eng));
		mSequence = Integer.valueOf(entry.getCustomElements().getValue(context.getString(R.string.column_category_sequence)));
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
