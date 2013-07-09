package cz.ictsystem.lib;

import android.content.Context;

public class Column {
	private Context mContext;
	private int mNameId;
	private String mName;
	private ColumnType mType;
	private int mTitle;
	
	public Column(Context context, int name, ColumnType type, int title){
		mContext = context;
		mNameId = name;
		mName = mContext.getString(name);
		mType = type;
	}
	
	public int getNameId(){
		return mNameId;
	}
	
	public String getName(){
		return mName;
	}

	public ColumnType getType(){
		return mType;
	}
	
	public int getTitle(){
		return mTitle;
	}
}
