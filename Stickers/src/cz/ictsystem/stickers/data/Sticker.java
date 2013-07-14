package cz.ictsystem.stickers.data;

import java.util.HashMap;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import cz.ictsystem.stickers.R;
import cz.ictsystem.stickers.Utils;

/**
 * Sticker data wrapper. Sticker is read only (database and google document sheet).
 * 
 * @author frantisek.ciompa
 *
 */
public class Sticker {
	
	private int mId;
	private String mNameCZ;
	private String mNameENG;
	private String mDescriptionCZ;
	private String mDescriptionENG;
	private byte[] mImage;
	private float mPriceCZK;
	private float mPriceEUR;
	private String mExpeditionTime;
	private boolean mEditableColor;
	private boolean mFeatured;
	private boolean mPopular;
	private boolean mNew;
	private String mUrl;
	
	public Sticker(){
		initializationAtributes();
	}

	public Sticker(int id, String nameCZ, String nameENG, String descriptionCZ, String descriptionENG, byte[] image, float priceCZK, float priceEUR, String expeditionTime, 
			boolean editableColor, boolean featured, boolean popular, boolean newSticker, String url){
		mId = id;
		mNameCZ = nameCZ;
		mNameENG = nameENG;
		mDescriptionCZ = descriptionCZ;
		mImage = image;
		mPriceCZK = priceCZK;
		mPriceEUR = priceEUR;
		mExpeditionTime = expeditionTime;
		mEditableColor = editableColor;
		mFeatured = featured;
		mPopular = popular;
		mNew = newSticker;
		mUrl = url;
	}

	public Sticker(Context context, HashMap<String, String> elements){
		mId = Integer.valueOf(elements.get(context.getString(R.string.column_id).toLowerCase(Locale.US)));
		mNameCZ = elements.get(context.getString(R.string.column_sticker_name_cz).toLowerCase(Locale.US));
		mNameENG = elements.get(context.getString(R.string.column_sticker_name_eng).toLowerCase(Locale.US));
		mDescriptionCZ = elements.get(context.getString(R.string.column_sticker_description_cz).toLowerCase(Locale.US));
		mDescriptionENG = elements.get(context.getString(R.string.column_sticker_description_eng).toLowerCase(Locale.US));
//		String img = entry.getCustomElements().getValue(context.getString(R.string.column_sticker_image)); 
//		mImage = img == null ? null : img.getBytes();
		String priceCZK = elements.get(context.getString(R.string.column_sticker_priceCZK).toLowerCase(Locale.US));
		mPriceCZK = Float.valueOf(priceCZK.replace(",", "."));
		String priceEUR = elements.get(context.getString(R.string.column_sticker_priceEUR).toLowerCase(Locale.US)); 
		mPriceEUR = Float.valueOf(priceEUR.replace(",", "."));
		mExpeditionTime = elements.get(context.getString(R.string.column_sticker_expedition_time).toLowerCase(Locale.US));
		mEditableColor = Boolean.valueOf(elements.get(context.getString(R.string.column_sticker_editable_color).toLowerCase(Locale.US)));
		mFeatured = Boolean.valueOf(elements.get(context.getString(R.string.column_sticker_featured).toLowerCase(Locale.US)));
		mPopular = Boolean.valueOf(elements.get(context.getString(R.string.column_sticker_popular).toLowerCase(Locale.US)));
		mNew = Boolean.valueOf(elements.get(context.getString(R.string.column_sticker_new).toLowerCase(Locale.US)));
		mUrl = elements.get(context.getString(R.string.column_sticker_url).toLowerCase(Locale.US));
	}

	public Sticker (Context context, int id){
		if(id != 0){
			Cursor c = context.getContentResolver().query(
					Uri.withAppendedPath(DbProvider.URI_STICKER, String.valueOf(id)), 
					null, null, null, null);
			setFromCursor(c);
			c.close();
		} else {
			initializationAtributes();
		}
	}

	public Sticker(Cursor cursor){
		setFromCursor(cursor);
	}

	private void initializationAtributes() {
		mId = 0;
		mNameCZ = null;
		mNameENG = null;
		mDescriptionCZ = null;
		mDescriptionENG = null;
		mImage = null;
		mPriceCZK = 0;
		mPriceEUR = 0;
		mExpeditionTime = null;
		mEditableColor = false;
		mFeatured = false;
		mPopular = false;
		mNew = false;
		mUrl = null;
	}

	public void setFromCursor(Cursor cursor) {
		mId = cursor.getInt(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_sticker, R.string.column_id)));
		mNameCZ = cursor.getString(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_sticker, R.string.column_sticker_name_cz)));
		mNameENG = cursor.getString(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_sticker, R.string.column_sticker_name_eng)));
		mDescriptionCZ = cursor.getString(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_sticker, R.string.column_sticker_description_cz)));
		mDescriptionENG = cursor.getString(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_sticker, R.string.column_sticker_description_eng)));
		mImage = cursor.getBlob(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_sticker, R.string.column_sticker_image))); 
		mPriceCZK = cursor.getFloat(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_sticker, R.string.column_sticker_priceCZK)));
		mPriceEUR = cursor.getFloat(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_sticker, R.string.column_sticker_priceEUR)));
		mExpeditionTime = cursor.getString(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_sticker, R.string.column_sticker_expedition_time)));
		mEditableColor = cursor.getString(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_sticker, R.string.column_sticker_editable_color))).equals("1");
		mFeatured = cursor.getString(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_sticker, R.string.column_sticker_featured))).equals("1");
		mPopular = cursor.getString(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_sticker, R.string.column_sticker_popular))).equals("1");
		mNew = cursor.getString(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_sticker, R.string.column_sticker_new))).equals("1");
		mUrl = cursor.getString(cursor.getColumnIndex(DbStickers.getColumnName(R.string.table_sticker, R.string.column_sticker_url)));
	}
	
	public void setImage(byte[]  image) {
		mImage = image;
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

	public String getName(){
		return Utils.isCZ() ? mNameCZ : mNameENG;
	}
	
	public String getDescriptionCZ() {
		return mDescriptionCZ;
	}

	public String getDescriptionENG() {
		return mDescriptionENG;
	}
	
	public String getDescription(){
		return Utils.isCZ() ? mDescriptionCZ : mDescriptionENG;
	}

	public byte[] getImage() {
		return mImage;
	}

	public Float getPriceCZK() {
		return mPriceCZK;
	}

	public Float getPriceEUR() {
		return mPriceEUR;
	}

	public boolean getEditableColor() {
		return mEditableColor;
	}

	public boolean getFeatured() {
		return mFeatured;
	}

	public boolean getPopular() {
		return mPopular;
	}

	public boolean getNew() {
		return mNew;
	}

	public String getUrl() {
		return mUrl;
	}
	
	public String getExpeditionTime(){
		return mExpeditionTime;
	}

	public ContentValues getSticker(Context context){
		final ContentValues contentValues = new ContentValues();
		contentValues.put(context.getString(R.string.column_id), mId);
		contentValues.put(context.getString(R.string.column_sticker_name_cz), mNameCZ);
		contentValues.put(context.getString(R.string.column_sticker_name_eng), mNameENG);
		contentValues.put(context.getString(R.string.column_sticker_description_cz), mDescriptionCZ);
		contentValues.put(context.getString(R.string.column_sticker_description_eng), mDescriptionENG);
		contentValues.put(context.getString(R.string.column_sticker_image), mImage);
		contentValues.put(context.getString(R.string.column_sticker_priceCZK), mPriceCZK);
		contentValues.put(context.getString(R.string.column_sticker_priceEUR), mPriceEUR);
		contentValues.put(context.getString(R.string.column_sticker_expedition_time), mExpeditionTime);
		contentValues.put(context.getString(R.string.column_sticker_editable_color), mEditableColor);
		contentValues.put(context.getString(R.string.column_sticker_featured), mFeatured);
		contentValues.put(context.getString(R.string.column_sticker_popular), mPopular);
		contentValues.put(context.getString(R.string.column_sticker_new), mNew);
		contentValues.put(context.getString(R.string.column_sticker_url), mUrl);
		
		return contentValues;
	}
}
