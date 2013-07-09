package cz.ictsystem.stickers;

import java.sql.Date;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.google.gdata.data.spreadsheet.ListEntry;

import cz.ictsystem.stickers.data.DbProvider;
import cz.ictsystem.stickers.data.DbStickers;

/**
 * Sticker properties (parameters) are used for sticker modification (manipulation) 
 * with is perform during visualization building.   
 * 
 * @author frantisek.ciompa
 *
 */
public class Visualization {

	/**
	 * ID visualization
	 */
	private int mId;
	
	/**
	 * Czech user visualization name
	 */
	private String mNameCZ;
	
	/**
	 * English user visualization name
	 */
	private String mNameENG;
	
	/**
	 * image URL for demo visualization background  
	 */
	private String mUrl;
	
	/**
	 * Visualization creation date 
	 */
	private Date mCreateDate;
	
	/**
	 * Visualization last update date 
	 */
	private Date mUpdateDate;
	
	/**
	 * Blob of background. Blob is read from database, where is saved during
	 * synchronization (for demo visualizations) or after taking picture by camera or
	 * after choosing picture from gallery.   
	 */
	private byte[] mBackground;
	
	
	/**
	 * Visualization Bitmap image that is made by Visualization builder. Image is saved into database,
	 * so gallery can be smooth. Image is saved into database during synchronization (for demo 
	 * visualizations) or after completion of visualization editing. 
	 */
	private Bitmap mImage;
	
	/**
	 * Sticker id used in visualization 
	 */
	private int mStickerId;
	
	/**
	 * Sticker x position in percent of image visualization size 
	 */
	private int mX;

	/**
	 * Sticker y position in percent of image visualization size
	 */
	private int mY;
	
	/**
	 * Sticker size in percentage of image visualization
	 */
	private int mSize;
	/**
	 * Actual color item. It is used for in items that have enable to change color 
	 */
	private int mColor;
	private int mPerspective;
	private boolean mFliped;
	
	public Visualization(){
		mId = 0;
		mNameCZ = null;
		mNameENG = null;
		mUrl = null;
		mCreateDate = new Date(System.currentTimeMillis());
		mUpdateDate = new Date(System.currentTimeMillis());
		mImage = null;
		mStickerId = 0;
		mX = 50;
		mY = 50;
		mSize = 50;
		mColor = 0;
		mPerspective = 0;
		mFliped = false;
	}

	public Visualization(Context context){
		this();
		mNameCZ = context.getString(R.string.visualization_detail_new_visualization_name);
		mNameENG = context.getString(R.string.visualization_detail_new_visualization_name);
	}

	public Visualization(int id){
		this();
		mId = id;
	}

	public Visualization(int id, String nameCZ, String nameENG, int stickerId, String url, byte[] background, 
			Date createDate, Date updateDate, int x, int y, int size, int color, int perspective, boolean fliped){
		mId = id;
		mNameCZ = nameCZ;
		mNameENG = nameENG;
		mUrl = url;
		mCreateDate = createDate;
		mUpdateDate = updateDate;
		mImage = null;
		mBackground = background;
		mStickerId = stickerId;
		mX = x;
		mY = y;
		mSize = size;
		mColor = color;
		mPerspective = perspective;
		mFliped = fliped;
	}
	
	public Visualization(Context context, ListEntry entry){
		mId = Integer.valueOf(entry.getCustomElements().getValue("id"));
		mNameCZ = entry.getCustomElements().getValue(context.getString(R.string.column_visualization_name_cz));
		mNameENG = entry.getCustomElements().getValue(context.getString(R.string.column_visualization_name_eng));
		mCreateDate = Date.valueOf(entry.getCustomElements().getValue(context.getString(R.string.column_visualization_create_date)));
		mUpdateDate = Date.valueOf(entry.getCustomElements().getValue(context.getString(R.string.column_visualization_update_date)));
		
		mUrl = entry.getCustomElements().getValue(context.getString(R.string.column_visualization_url_background));
		mBackground = null;
		mImage = null;
		
		mStickerId = Integer.valueOf(entry.getCustomElements().getValue(context.getString(R.string.column_visualization_sticker_id)));
		mX = Integer.valueOf(entry.getCustomElements().getValue(context.getString(R.string.column_visualization_sticker_position_X)));
		mY = Integer.valueOf(entry.getCustomElements().getValue(context.getString(R.string.column_visualization_sticker_position_Y)));
		mSize = Integer.valueOf(entry.getCustomElements().getValue(context.getString(R.string.column_visualization_sticker_size)));
		mColor = Integer.valueOf(entry.getCustomElements().getValue(context.getString(R.string.column_visualization_sticker_color)));
		mPerspective = Integer.valueOf(entry.getCustomElements().getValue(context.getString(R.string.column_visualization_sticker_perspective)));
		mFliped = Boolean.valueOf(entry.getCustomElements().getValue(context.getString(R.string.column_visualization_sticker_flip)));
	}

	public Visualization(Context context, Cursor cursor){
		set(context, cursor);
	}

	public Visualization set(Context context, Cursor cursor) {
		mId = cursor.getInt(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_visualization, R.string.column_id)));
		mNameCZ = cursor.getString(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_visualization, R.string.column_visualization_name_cz)));
		mNameENG = cursor.getString(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_visualization, R.string.column_visualization_name_eng)));
		
		mStickerId = cursor.getInt(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_visualization, R.string.column_visualization_sticker_id)));
		
//		mUrl = cursor.getString(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_visualization, R.string.column_visualization_url)));
		mBackground = cursor.getBlob(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_visualization, R.string.column_visualization_background)));
		
		byte[] imageBlob = cursor.getBlob(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_visualization, R.string.column_visualization_image)));
		mImage = Utils.getBitmapFromBlob(imageBlob, Utils.getDisplaySize(context).x, Utils.getDisplaySize(context).y);
		
		mCreateDate = Date.valueOf(cursor.getString(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_visualization, R.string.column_visualization_create_date))));
		mUpdateDate = Date.valueOf(cursor.getString(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_visualization, R.string.column_visualization_update_date))));
		
		mX = cursor.getInt(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_visualization, R.string.column_visualization_sticker_position_X)));
		mY = cursor.getInt(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_visualization, R.string.column_visualization_sticker_position_Y)));
		mSize = cursor.getInt(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_visualization, R.string.column_visualization_sticker_size)));
		mColor = cursor.getInt(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_visualization, R.string.column_visualization_sticker_color)));
		mPerspective = cursor.getInt(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_visualization, R.string.column_visualization_sticker_perspective)));
		mFliped = cursor.getString(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_visualization, R.string.column_visualization_sticker_flip))).equals("1");
		
		return this;
	}
	
	public int getId() {
		return mId;
	}

	public String getNameCZ() {
		return mNameCZ;
	}

	public String getNameENG() {
		return mNameENG;
	}

	public String getName() {
		return Utils.isCZ() ? mNameCZ : mNameENG;
	}

	public Visualization setName(String name) {
		mNameCZ = name;
		mNameENG = name;
		return this;
	}

	public String getUrl() {
		return mUrl;
	}

	public byte[] getBackground() {
		return mBackground;
	}

	public Date getCreateDate() {
		return mCreateDate;
	}

	public Date getUpdateDate() {
		return mUpdateDate;
	}

	public int getStickerId(){
		return mStickerId;
	}

	public Visualization setBackground(byte[] background) {
		mBackground = background;
		return this;
	}
	
	public Visualization setBackground(Drawable drawable) {
		mBackground = Utils.getBlobJPEG(((BitmapDrawable)drawable).getBitmap());
		return this;
	}
	
	public Bitmap getImage(){
		return mImage;
	}

	public Visualization setImage(Bitmap image){
		mImage = image;
		return this;
	}

	public Visualization setStickerId(int stickerId){
		mStickerId = stickerId;
		return this;
	}
	
	public Visualization save(Context context){
		ContentResolver contentResolver = context.getContentResolver();
    	Cursor cursor = context.getContentResolver().query(Uri.withAppendedPath(DbProvider.URI_VISUALIZATION, String.valueOf(mId)), 
    			null, null, null, null); 
    	if(cursor.getCount() == 1){
    		mUpdateDate = new Date(System.currentTimeMillis());
    		contentResolver.update(Uri.withAppendedPath(DbProvider.URI_VISUALIZATION, String.valueOf(mId)),  getContentValues(context), null, null);
    	} else {
    		Uri uri = contentResolver.insert(DbProvider.URI_VISUALIZATION,  getContentValues(context));
    		mId = Integer.valueOf(uri.getLastPathSegment());
    	}
    	cursor.close();
    	return this;
	}

	public ContentValues getContentValues(Context context){
		final ContentValues contentValues = new ContentValues();
		if(mId != 0){
			contentValues.put(context.getString(R.string.column_id), mId);	
		} else {
			//not inserted record, yet
			contentValues.putNull(context.getString(R.string.column_id));
		}
		
		contentValues.put(context.getString(R.string.column_visualization_name_cz), mNameCZ);
		contentValues.put(context.getString(R.string.column_visualization_name_eng), mNameENG);
		contentValues.put(context.getString(R.string.column_visualization_sticker_id), mStickerId);
//		contentValues.put(context.getString(R.string.column_visualization_url), mUrl);
		if (mBackground != null){
			contentValues.put(context.getString(R.string.column_visualization_background), mBackground);
		}
			
		if (mImage != null){
			byte[] imageBlob = Utils.getBlobJPEG(mImage);
			contentValues.put(context.getString(R.string.column_visualization_image), imageBlob);
		}
			
		contentValues.put(context.getString(R.string.column_visualization_create_date), mCreateDate.toString());
		contentValues.put(context.getString(R.string.column_visualization_update_date), mUpdateDate.toString());
		
		contentValues.put(context.getString(R.string.column_visualization_sticker_position_X), mX);
		contentValues.put(context.getString(R.string.column_visualization_sticker_position_Y), mY);
		contentValues.put(context.getString(R.string.column_visualization_sticker_size), mSize);
		contentValues.put(context.getString(R.string.column_visualization_sticker_color), mColor);
		contentValues.put(context.getString(R.string.column_visualization_sticker_perspective), mPerspective);
		contentValues.put(context.getString(R.string.column_visualization_sticker_flip), mFliped);
		return contentValues;
	}
	
	public void delete(Context context){
		context.getContentResolver().delete(
				Uri.withAppendedPath(DbProvider.URI_VISUALIZATION,String.valueOf(mId)), 
				null, null);
		mId = 0;
	}
	
	public int getX() {
		return mX;
	}

	public int getY() {
		return mY;
	}

	public int getSize() {
		return mSize;
	}

	public int getColor(){
		return mColor;
	}
	
	public int getPerspective() {
		return mPerspective;
	}

	public Visualization setX(int x) {
		mX = x;
		return this;
	}

	public Visualization setY(int y) {
		mY = y;
		return this;
	}

	public Visualization setSize(int size) {
		mSize = size;
		return this;
	}

	public Visualization setColor(int color) {
		mColor = color;
		return this;
	}

	public Visualization setColor() {
		mColor = 0;
		return this;
	}
	public Visualization setPerspective(int perspective) {
		mPerspective = perspective;
		return this;
	}
	
	public Visualization setFliped(boolean fliped){
		mFliped = fliped;
		return this;
	}
	public boolean getFliped(){
		return mFliped;
	}

}
