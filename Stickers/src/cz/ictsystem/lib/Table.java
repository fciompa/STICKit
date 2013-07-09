package cz.ictsystem.lib;

import java.util.Hashtable;

import android.content.Context;
import cz.ictsystem.stickers.R;


public class Table {
	
	private Context mContext; 
	private int mNameId;
	private String mName;
	private Hashtable<Integer, Column> mColumns;
	
	public Table(Context context, int name){
		mColumns = new Hashtable<Integer, Column>();
		mContext = context;
		mNameId = name;
		mName = mContext.getString(name);
		mColumns.put(R.string.column_id, new Column(mContext, R.string.column_id, ColumnType.ID, R.string.id_column_title));
	}
	
	public Table addColumn(Column column){
		mColumns.put(column.getNameId(), column);
		return this;
	}
	
	public String getName(){
		return mName;
	}
	
	public int getNameId(){
		return mNameId;
	}
	
	public Column getColumn(int name){
		return mColumns.get(name);
	}
	
	public Hashtable<Integer, Column> getColumns(){
		return mColumns;
	}
}
