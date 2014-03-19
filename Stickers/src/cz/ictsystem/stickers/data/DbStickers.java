package cz.ictsystem.stickers.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import cz.ictsystem.lib.Column;
import cz.ictsystem.lib.ColumnType;
import cz.ictsystem.lib.DbStructure;
import cz.ictsystem.lib.Table;
import cz.ictsystem.stickers.R;

public class DbStickers extends SQLiteAssetHelper {
	private static final String DATABASE_NAME = "nalepShop";
	private static final int DATABASE_VERSION = 1;
//	private static final String TAG = "DbStickers";
	
	private static Context mContext;
	private static DbStructure mStructure;
	private static SQLiteDatabase mDb;
	
	DbStickers(Context context){
		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		mContext = context;
	
		mStructure = new DbStructure()
		.addTable(new Table(mContext, R.string.table_category)
			.addColumn(new Column(mContext, R.string.column_category_name_cz, ColumnType.STRING, R.string.column_category_name_cz))
			.addColumn(new Column(mContext, R.string.column_category_name_eng, ColumnType.STRING, R.string.column_category_name_eng))
			.addColumn(new Column(mContext, R.string.column_category_sequence, ColumnType.INT, R.string.column_category_sequence)))
		.addTable(new Table(mContext, R.string.table_sticker)
			.addColumn(new Column(mContext, R.string.column_sticker_name_cz, ColumnType.STRING, R.string.column_sticker_name_cz))
			.addColumn(new Column(mContext, R.string.column_sticker_name_eng, ColumnType.STRING, R.string.column_sticker_name_eng))
			.addColumn(new Column(mContext, R.string.column_sticker_description_cz, ColumnType.STRING, R.string.column_sticker_description_cz))
			.addColumn(new Column(mContext, R.string.column_sticker_description_eng, ColumnType.STRING, R.string.column_sticker_description_eng))
			.addColumn(new Column(mContext, R.string.column_sticker_image, ColumnType.BLOB, R.string.column_sticker_image))
			.addColumn(new Column(mContext, R.string.column_sticker_priceCZK, ColumnType.FLOAT, R.string.column_sticker_priceCZK))
			.addColumn(new Column(mContext, R.string.column_sticker_priceEUR, ColumnType.FLOAT, R.string.column_sticker_priceEUR))
			.addColumn(new Column(mContext, R.string.column_sticker_expedition_time, ColumnType.STRING, R.string.column_sticker_expedition_time))
			.addColumn(new Column(mContext, R.string.column_sticker_editable_color, ColumnType.BOOLEAN, R.string.column_sticker_editable_color))
			.addColumn(new Column(mContext, R.string.column_sticker_featured, ColumnType.BOOLEAN, R.string.column_sticker_featured))
			.addColumn(new Column(mContext, R.string.column_sticker_popular, ColumnType.BOOLEAN, R.string.column_sticker_popular))
			.addColumn(new Column(mContext, R.string.column_sticker_new, ColumnType.BOOLEAN, R.string.column_sticker_new))
			.addColumn(new Column(mContext, R.string.column_sticker_url, ColumnType.STRING, R.string.column_sticker_url)))
		.addTable(new Table(mContext, R.string.table_stickercategory)
			.addColumn(new Column(mContext, R.string.column_stickercategory_category_id, ColumnType.INT, R.string.column_stickercategory_category_id))
			.addColumn(new Column(mContext, R.string.column_stickercategory_sticker_id, ColumnType.INT, R.string.column_stickercategory_sticker_id)))
		.addTable(new Table(mContext, R.string.table_visualization)
			.addColumn(new Column(mContext, R.string.column_visualization_name_cz, ColumnType.STRING, R.string.column_visualization_name_cz))
			.addColumn(new Column(mContext, R.string.column_visualization_name_eng, ColumnType.STRING, R.string.column_visualization_name_eng))
			.addColumn(new Column(mContext, R.string.column_visualization_create_date, ColumnType.DATETIME, R.string.column_visualization_create_date))
			.addColumn(new Column(mContext, R.string.column_visualization_update_date, ColumnType.DATETIME, R.string.column_visualization_update_date))
//			.addColumn(new Column(mContext, R.string.column_visualization_url, ColumnType.STRING, R.string.column_visualization_url))
			.addColumn(new Column(mContext, R.string.column_visualization_sticker_id, ColumnType.INT, R.string.column_visualization_sticker_id))
			.addColumn(new Column(mContext, R.string.column_visualization_sticker_position_X, ColumnType.INT, R.string.column_visualization_sticker_position_X))
			.addColumn(new Column(mContext, R.string.column_visualization_sticker_position_Y, ColumnType.INT, R.string.column_visualization_sticker_position_Y))
			.addColumn(new Column(mContext, R.string.column_visualization_sticker_size, ColumnType.INT, R.string.column_visualization_sticker_size))
			.addColumn(new Column(mContext, R.string.column_visualization_sticker_perspective, ColumnType.INT, R.string.column_visualization_sticker_perspective))
			.addColumn(new Column(mContext, R.string.column_visualization_sticker_flip, ColumnType.BOOLEAN, R.string.column_visualization_sticker_flip))
			.addColumn(new Column(mContext, R.string.column_visualization_sticker_color, ColumnType.INT, R.string.column_visualization_sticker_color))
			.addColumn(new Column(mContext, R.string.column_visualization_background, ColumnType.BLOB, R.string.column_visualization_background))
			.addColumn(new Column(mContext, R.string.column_visualization_image, ColumnType.BLOB, R.string.column_visualization_image))); 
	}
	
	public static DbStructure getDbStructure(Context context){
		return mStructure;
	}

	public static String getColumnName(int table, int column){
		return mStructure.getTable(table).getColumn(column).getName();
	}
	
//	@Override
//	public void onCreate(SQLiteDatabase db) {
//		createTables(db, getDbStructure(mContext));
//		mContext.startService(new Intent(mContext, SyncService.class));
//	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
//	private void createTables(SQLiteDatabase db, DbStructure dbStructure){
//		Log.d(TAG, "createTables");
//		for(Table table : dbStructure.getTables().values()){
//			createTable(db, table);
//		}
//		
//	}

//	private void createTable(SQLiteDatabase db, Table table){
//		Log.d(TAG, table.getName());
//		String sql = "CREATE TABLE " + table.getName() + "( _id INTEGER PRIMARY KEY AUTOINCREMENT";
//		for(Column column : table.getColumns().values()){
//			if (column.getType() != ColumnType.ID){
//				if (column.getType() == ColumnType.STRING){
//					sql = sql + ", " + column.getName() + " TEXT NOT NULL ";
//				} else if (column.getType() == ColumnType.INT){
//					sql = sql + ", " + column.getName() + " INTEGER NOT NULL ";
//				} else if (column.getType() == ColumnType.FLOAT){
//					sql = sql + ", " + column.getName() + " REAL NOT NULL ";
//				} else if (column.getType() == ColumnType.DATE){
//					sql = sql + ", " + column.getName() + " TEXT NOT NULL ";
//				} else if (column.getType() == ColumnType.DATETIME){
//					sql = sql + ", " + column.getName() + " TEXT NOT NULL ";
//				} else if (column.getType() == ColumnType.BLOB){
//					sql = sql + ", " + column.getName() + " BLOB ";
//				} else if (column.getType() == ColumnType.BOOLEAN){
//					sql = sql + ", " + column.getName() + " INTEGER NOT NULL ";
//				} else {
//					throw new IllegalArgumentException("Unknown Column data type");
//				}
//				
//			}
//		}
//		sql = sql + ");";
//		Log.d(TAG, sql);
//		db.execSQL(sql);
//    }
	
	public Boolean openDatabase(){
        mDb = getWritableDatabase();
        return mDb != null;
	}
	
	public Cursor fetchCategories(){
		Cursor c = mDb.query(mStructure.getTable(R.string.table_category).getName(), null, null, null, null, null, 
				mStructure.getTable(R.string.table_category).getColumn(R.string.column_category_sequence).getName()); 
		c.moveToFirst();
		return c;
	}

	public Cursor fetchCategoryId(int id){
		StringBuilder whereClause = new StringBuilder(mContext.getString(R.string.column_id)).append("=").append(id);
		Cursor c = mDb.query(mStructure.getTable(R.string.table_category).getName(), null, whereClause.toString(), null, null, null, 
				mStructure.getTable(R.string.table_category).getColumn(R.string.column_category_sequence).getName());
		moveFirst(c);
		return c;
	}
	
	public long insertCategory(ContentValues values){
		return mDb.insert(mStructure.getTable(R.string.table_category).getName(), null, values);
	}
	
	public int updateCategory(int id, ContentValues values){
		StringBuilder whereClause = new StringBuilder(mContext.getString(R.string.column_id)).append("=").append(id);
		return mDb.update(mStructure.getTable(R.string.table_category).getName(), values, whereClause.toString(), null);
	}	
	
	public int deleteCategory(int id){
		StringBuilder whereClause = new StringBuilder(mContext.getString(R.string.column_id)).append("=").append(id);
		return mDb.delete(mStructure.getTable(R.string.table_category).getName(), whereClause.toString(), null);
	}

	public Cursor fetchStickers(){
		Cursor c = mDb.query(mStructure.getTable(R.string.table_sticker).getName(), null, null, null, null, null, 
				mStructure.getTable(R.string.table_sticker).getColumn(R.string.column_sticker_name_cz).getName());
		moveFirst(c);
		return c;
	}
	
	public Cursor fetchStickerId(int id){
		StringBuilder whereClause = new StringBuilder(mContext.getString(R.string.column_id)).append("=").append(id);
		Cursor c = mDb.query(mStructure.getTable(R.string.table_sticker).getName(), null, whereClause.toString(), null, null, null, 
				mStructure.getTable(R.string.table_sticker).getColumn(R.string.column_sticker_name_cz).getName());
		moveFirst(c);
		return c;
	}
	
	public Cursor fetchFeaturedStickers(){
		StringBuilder whereClause = new StringBuilder(mStructure.getTable(R.string.table_sticker).getColumn(
				R.string.column_sticker_featured).getName()).append(" = '1'");
		Cursor c = mDb.query(mStructure.getTable(R.string.table_sticker).getName(), null, whereClause.toString(), null, null, null, 
				mStructure.getTable(R.string.table_sticker).getColumn(R.string.column_sticker_name_cz).getName()); 
		moveFirst(c);
		return c;
	}
	
	public Cursor fetchPopularStickers(){
		StringBuilder whereClause = new StringBuilder(mStructure.getTable(R.string.table_sticker).getColumn(
				R.string.column_sticker_popular).getName()).append(" = '1'");
		Cursor c = mDb.query(mStructure.getTable(R.string.table_sticker).getName(), null, whereClause.toString(), null, null, null, 
				mStructure.getTable(R.string.table_sticker).getColumn(R.string.column_sticker_name_cz).getName());
		moveFirst(c);
		return c;
	}
	
	public Cursor fetchNewStickersy(){
		StringBuilder whereClause = new StringBuilder(mStructure.getTable(R.string.table_sticker).getColumn(
				R.string.column_sticker_new).getName()).append(" = '1'");
		Cursor c = mDb.query(mStructure.getTable(R.string.table_sticker).getName(), null, whereClause.toString(), null, null, null, 
				mStructure.getTable(R.string.table_sticker).getColumn(R.string.column_sticker_name_cz).getName());
		moveFirst(c);
		return c;
	}
	
	private String getWhereClauseStickersByCategory(int categoryId) {
		String whereClause = new StringBuilder(
				mContext.getString(R.string.column_id))
				.append(" in (select ")
				.append(mContext.getString(R.string.column_stickercategory_sticker_id))
				.append(" from ")
				.append(mContext.getString(R.string.table_stickercategory))
				.append(" where ")
				.append(mContext.getString(R.string.column_stickercategory_category_id))
				.append(" = '")
				.append(categoryId)
				.append("')")
				.toString();
		return whereClause;
	}
	
	public Cursor fetchStickersByCategory(int categoryId){
		String whereClause = getWhereClauseStickersByCategory(categoryId);
		String orderByClause = mStructure.getTable(R.string.table_sticker).getColumn(R.string.column_sticker_name_cz).getName();
		Cursor c = mDb.query(mStructure.getTable(R.string.table_sticker).getName(), null, whereClause, null, null, null, orderByClause); 
		moveFirst(c);
		return c;
	}
	
	public Cursor fetchPopularStickersByCategory(int categoryId){
		String whereClause = new StringBuilder(getWhereClauseStickersByCategory(categoryId))
				.append(" and ")
				.append(mStructure.getTable(R.string.table_sticker).getColumn(R.string.column_sticker_popular).getName())
				.append(" = '1'")
				.toString();
		
		String orderByClause = mStructure.getTable(R.string.table_sticker).getColumn(R.string.column_sticker_name_cz).getName();
		Cursor c = mDb.query(mStructure.getTable(R.string.table_sticker).getName(), null, whereClause, null, null, null, orderByClause);
		moveFirst(c);
		return c;
	}
	
	public Cursor fetchNewStickersByCategory(int categoryId){
		String whereClause = new StringBuilder(getWhereClauseStickersByCategory(categoryId))
				.append(" and ")
				.append(mStructure.getTable(R.string.table_sticker).getColumn(R.string.column_sticker_new).getName())
				.append(" = '1'")
				.toString();
		
		String orderByClause = mStructure.getTable(R.string.table_sticker).getColumn(R.string.column_sticker_name_cz).getName();
		Cursor c = mDb.query(mStructure.getTable(R.string.table_sticker).getName(), null, whereClause, null, null, null, orderByClause);
		moveFirst(c);
		return c;
	}
	
	public Cursor fetchFeaturedStickersByCategory(int categoryId){
		String whereClause = new StringBuilder(getWhereClauseStickersByCategory(categoryId))
				.append(" and ")
				.append(mStructure.getTable(R.string.table_sticker).getColumn(R.string.column_sticker_featured).getName())
				.append(" = '1'")
				.toString();
		
		String orderByClause = mStructure.getTable(R.string.table_sticker).getColumn(R.string.column_sticker_name_cz).getName();
		Cursor c = mDb.query(mStructure.getTable(R.string.table_sticker).getName(), null, whereClause.toString(), null, null, null, orderByClause);
		moveFirst(c);
		return c;
	}

	public long insertSticker(ContentValues values){
		return mDb.insert(mStructure.getTable(R.string.table_sticker).getName(), null, values);
	}
	
	public int updateSticker(int id, ContentValues values){
		StringBuilder whereClause = new StringBuilder(mContext.getString(R.string.column_id)).append("=").append(id);
		return mDb.update(mStructure.getTable(R.string.table_sticker).getName(), values, whereClause.toString(), null);
	}	
	
	public int deleteSticker(int id){
		StringBuilder whereClause = new StringBuilder(mContext.getString(R.string.column_id)).append("=").append(id);
		return mDb.delete(mStructure.getTable(R.string.table_sticker).getName(), whereClause.toString(), null);
	}

	public Cursor fetchStickerCategory(){
		StringBuilder orderByClause = new StringBuilder(mContext.getString(R.string.column_id));
		Cursor c = mDb.query(mStructure.getTable(R.string.table_stickercategory).getName(), null, null, null, null, null, orderByClause.toString());
		moveFirst(c);
		return c;
	}
	
	public Cursor fetchStickerCategoryId(int id){
		StringBuilder whereClause = new StringBuilder(mContext.getString(R.string.column_id)).append("=").append(id);
		Cursor c = mDb.query(mStructure.getTable(R.string.table_stickercategory).getName(), null, whereClause.toString(), null, null, null, null);
		moveFirst(c);
		return c;
	}
	
	public long insertStickerCategory(ContentValues values){
		return mDb.insert(mStructure.getTable(R.string.table_stickercategory).getName(), null, values);
	}
	
	public int updateStickerCategory(int id, ContentValues values){
		StringBuilder whereClause = new StringBuilder(mContext.getString(R.string.column_id)).append("=").append(id);
		return mDb.update(mStructure.getTable(R.string.table_stickercategory).getName(), values, whereClause.toString(), null);
	}	
	
	public int deleteStickerCategory(int id){
		StringBuilder whereClause = new StringBuilder(mContext.getString(R.string.column_id)).append("=").append(id);
		return mDb.delete(mStructure.getTable(R.string.table_stickercategory).getName(), whereClause.toString(), null);
	}

	public Cursor fetchVisualizations(){
		//StringBuilder orderByClause = new StringBuilder(mContext.getString(R.string.column_visualization_name));
		StringBuilder orderByClause = new StringBuilder(mContext.getString(R.string.column_id)).append(" desc");
		Cursor c = mDb.query(mStructure.getTable(R.string.table_visualization).getName(), null, null, null, null, null, orderByClause.toString());
		moveFirst(c);
		return c;
	}
	
	public Cursor fetchVisualizationId(int id){
		StringBuilder whereClause = new StringBuilder(mContext.getString(R.string.column_id)).append("=").append(id);
		Cursor c = mDb.query(mStructure.getTable(R.string.table_visualization).getName(), null, whereClause.toString(), null, null, null, null);
		moveFirst(c);
		return c;
	}

	public long insertVisualization(ContentValues values){
		return mDb.insert(mStructure.getTable(R.string.table_visualization).getName(), null, values);
	}
	
	public int updateVisualization(int id, ContentValues values){
		StringBuilder whereClause = new StringBuilder(mContext.getString(R.string.column_id)).append("=").append(id);
		return mDb.update(mStructure.getTable(R.string.table_visualization).getName(), values, whereClause.toString(), null);
	}

	public int deleteVisualization(int id){
		StringBuilder whereClause = new StringBuilder(mContext.getString(R.string.column_id)).append(" = ").append(id);
		return mDb.delete(mStructure.getTable(R.string.table_visualization).getName(), whereClause.toString(), null);
	}
	private void moveFirst(Cursor c) {
		if(c.getCount() > 0){
			c.moveToFirst();
		}
	}
}

