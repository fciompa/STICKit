package cz.ictsystem.stickers.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

public class DbProvider extends ContentProvider {
	
	private static final String AUTHORITY = "cz.ictsystem.stickers";
	private static final String CONTENT = "content://";
	private static final String CONTENT_AUTORITY = CONTENT + AUTHORITY + "/";
	private static final String ID = "/#";
	private static final String CATEGORY = "category";
	private static final String STICKER = "sticker";
	private static final String FEATURED = "featured";
	private static final String POPULAR = "popular";
	private static final String NEW = "new";
	private static final String STICKER_CATEGORY = "stickerCategory";
	private static final String VISUALIZATION = "visualization";
	
	public static final Uri URI_CATEGORY = Uri.parse(CONTENT_AUTORITY + CATEGORY);
	public static final Uri URI_STICKER = Uri.parse(CONTENT_AUTORITY + STICKER);
	public static final Uri URI_FEATURED_STICKER = Uri.parse(CONTENT_AUTORITY + STICKER + "/" + FEATURED);
	public static final Uri URI_POPULAR_STICKER = Uri.parse(CONTENT_AUTORITY + STICKER + "/" + POPULAR);
	public static final Uri URI_NEW_STICKER = Uri.parse(CONTENT_AUTORITY + STICKER + "/" + NEW);
	public static final Uri URI_STICKERS_BY_CATEGORY = Uri.parse(CONTENT_AUTORITY + STICKER + "/" + CATEGORY);
	public static final Uri URI_FEATURED_STICKERS_BY_CATEGORY = Uri.parse(CONTENT_AUTORITY + STICKER + "/" + FEATURED + "/" + CATEGORY);
	public static final Uri URI_POPULAR_STICKER_BY_CATEGORY = Uri.parse(CONTENT_AUTORITY + STICKER + "/" + POPULAR + "/" + CATEGORY);
	public static final Uri URI_NEW_STICKERS_BY_CATEGORY = Uri.parse(CONTENT_AUTORITY + STICKER + "/" + NEW + "/" + CATEGORY);
	public static final Uri URI_STICKER_CATEGORY = Uri.parse(CONTENT_AUTORITY + STICKER_CATEGORY);
	public static final Uri URI_VISUALIZATION = Uri.parse(CONTENT_AUTORITY + VISUALIZATION);
	
	
	private static final int URI_ID_CATEGORY = 1;
	private static final int URI_ID_CATEGORY_ID = 2;
	private static final int URI_ID_STICKER = 3;
	private static final int URI_ID_STICKER_ID = 4;
	private static final int URI_ID_FEATURET_STICKER = 5;
	private static final int URI_ID_POPULAR_STICKER = 6;
	private static final int URI_ID_NEW_STICKER = 7;
	private static final int URI_ID_STICKERS_BY_CATEGORY = 8;
	private static final int URI_ID_FEATURED_STICKERS_BY_CATEGORY = 9;
	private static final int URI_ID_POPULAR_STICKER_BY_CATEGORY = 10;
	private static final int URI_ID_NEW_STICKERS_BY_CATEGORY = 11;
	private static final int URI_ID_STICKER_CATEGORY = 12;
	private static final int URI_ID_STICKER_CATEGORY_ID = 13;
	private static final int URI_ID_VISUALIZATION = 14;
	private static final int URI_ID_VISUALIZATION_ID = 15;
	
	private static final UriMatcher uriMatcher;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, URI_CATEGORY.getPath().substring(1), URI_ID_CATEGORY);//nerozumím proè, ale druhý parametr nesmí zaèínat lomítkem a jak ho dostat bez lomítka nevím
		uriMatcher.addURI(AUTHORITY, URI_CATEGORY.getPath().substring(1) + ID, URI_ID_CATEGORY_ID);
		uriMatcher.addURI(AUTHORITY, URI_STICKER.getPath().substring(1), URI_ID_STICKER);
		uriMatcher.addURI(AUTHORITY, URI_STICKER.getPath().substring(1) + ID, URI_ID_STICKER_ID);
		uriMatcher.addURI(AUTHORITY, URI_FEATURED_STICKER.getPath().substring(1), URI_ID_FEATURET_STICKER);
		uriMatcher.addURI(AUTHORITY, URI_POPULAR_STICKER.getPath().substring(1), URI_ID_POPULAR_STICKER);
		uriMatcher.addURI(AUTHORITY, URI_NEW_STICKER.getPath().substring(1), URI_ID_NEW_STICKER);
		uriMatcher.addURI(AUTHORITY, URI_STICKERS_BY_CATEGORY.getPath().substring(1) + ID, URI_ID_STICKERS_BY_CATEGORY);
		uriMatcher.addURI(AUTHORITY, URI_FEATURED_STICKERS_BY_CATEGORY.getPath().substring(1) + ID, URI_ID_FEATURED_STICKERS_BY_CATEGORY);
		uriMatcher.addURI(AUTHORITY, URI_POPULAR_STICKER_BY_CATEGORY.getPath().substring(1) + ID, URI_ID_POPULAR_STICKER_BY_CATEGORY);
		uriMatcher.addURI(AUTHORITY, URI_NEW_STICKERS_BY_CATEGORY.getPath().substring(1) + ID, URI_ID_NEW_STICKERS_BY_CATEGORY);
		uriMatcher.addURI(AUTHORITY, URI_STICKER_CATEGORY.getPath().substring(1), URI_ID_STICKER_CATEGORY);
		uriMatcher.addURI(AUTHORITY, URI_STICKER_CATEGORY.getPath().substring(1) + ID, URI_ID_STICKER_CATEGORY_ID);
		uriMatcher.addURI(AUTHORITY, URI_VISUALIZATION.getPath().substring(1), URI_ID_VISUALIZATION);
		uriMatcher.addURI(AUTHORITY, URI_VISUALIZATION.getPath().substring(1) + ID, URI_ID_VISUALIZATION_ID);
	}
	
	
	private DbStickers mDb; 

	@Override
	public boolean onCreate() {
         mDb = new DbStickers(getContext());
        return mDb.openDatabase();
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Cursor c = null;
		switch (uriMatcher.match(uri)) {
			case URI_ID_CATEGORY:
				c = mDb.fetchCategories();
				break;
			case URI_ID_CATEGORY_ID:
				c = mDb.fetchCategoryId(Integer.valueOf(uri.getLastPathSegment()));
				break;
			case URI_ID_STICKER:
				c = mDb.fetchStickers();
				break;
			case URI_ID_STICKER_ID:
				c = mDb.fetchStickerId(Integer.valueOf(uri.getLastPathSegment()));
				break;
			case URI_ID_FEATURET_STICKER:
				c = mDb.fetchFeaturedStickers();
				break;
			case URI_ID_POPULAR_STICKER:
				c = mDb.fetchPopularStickers();
				break;
			case URI_ID_NEW_STICKER:
				c = mDb.fetchNewStickersy();
				break;
			case URI_ID_STICKERS_BY_CATEGORY:
				c = mDb.fetchStickersByCategory(Integer.valueOf(uri.getLastPathSegment()));
				break;
			case URI_ID_FEATURED_STICKERS_BY_CATEGORY:
				c = mDb.fetchFeaturedStickersByCategory(Integer.valueOf(uri.getLastPathSegment()));
				break;
			case URI_ID_POPULAR_STICKER_BY_CATEGORY:
				c = mDb.fetchPopularStickersByCategory(Integer.valueOf(uri.getLastPathSegment()));
				break;
			case URI_ID_NEW_STICKERS_BY_CATEGORY:
				c = mDb.fetchNewStickersByCategory(Integer.valueOf(uri.getLastPathSegment()));
				break;
			case URI_ID_STICKER_CATEGORY:
				c = mDb.fetchStickerCategory();
				break;
			case URI_ID_STICKER_CATEGORY_ID:
				c = mDb.fetchStickerCategoryId(Integer.valueOf(uri.getLastPathSegment()));
				break;
			case URI_ID_VISUALIZATION:
				c = mDb.fetchVisualizations();
				break;
			case URI_ID_VISUALIZATION_ID:
				c = mDb.fetchVisualizationId(Integer.valueOf(uri.getLastPathSegment()));
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long rowID = -1;
		Uri newUri = null;
		switch (uriMatcher.match(uri)) {
			case URI_ID_CATEGORY:
				rowID = mDb.insertCategory(values);
				newUri = ContentUris.withAppendedId(URI_CATEGORY, rowID);
				break;
			case URI_ID_STICKER:
				rowID = mDb.insertSticker(values);
				newUri = ContentUris.withAppendedId(URI_FEATURED_STICKERS_BY_CATEGORY, rowID);//ContentUris.withAppendedId(URI_STICKER, rowID);
				break;
			case URI_ID_STICKER_CATEGORY:
				rowID = mDb.insertStickerCategory(values);
				newUri = ContentUris.withAppendedId(URI_STICKER_CATEGORY, rowID);
				break;
			case URI_ID_VISUALIZATION:
				rowID = mDb.insertVisualization(values);
				newUri = ContentUris.withAppendedId(URI_VISUALIZATION, rowID);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
        if (rowID <= 0) {
        	throw new SQLException("Failed to insert row into " + uri);
        }

        
        getContext().getContentResolver().notifyChange(newUri, null);
        return newUri;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
		switch (uriMatcher.match(uri)) {
			case URI_ID_CATEGORY_ID:
				count = mDb.updateCategory(Integer.valueOf(uri.getLastPathSegment()), values);
				break;
			case URI_ID_STICKER_ID:
				count = mDb.updateSticker(Integer.valueOf(uri.getLastPathSegment()), values);
				break;
			case URI_ID_STICKER_CATEGORY_ID:
				count = mDb.updateStickerCategory(Integer.valueOf(uri.getLastPathSegment()), values);
				break;
			case URI_ID_VISUALIZATION_ID:
				count = mDb.updateVisualization(Integer.valueOf(uri.getLastPathSegment()), values);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
		switch (uriMatcher.match(uri)) {
			case URI_ID_CATEGORY_ID:
				count = mDb.deleteCategory(Integer.valueOf(uri.getLastPathSegment()));
				break;
			case URI_ID_STICKER_ID:
				count = mDb.deleteSticker(Integer.valueOf(uri.getLastPathSegment()));
				break;
			case URI_ID_STICKER_CATEGORY_ID:
				count = mDb.deleteStickerCategory(Integer.valueOf(uri.getLastPathSegment()));
				break;
			case URI_ID_VISUALIZATION_ID:
				count = mDb.deleteVisualization(Integer.valueOf(uri.getLastPathSegment()));
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
	}

	@Override
	public String getType(Uri uri) {
        String type = null;
		switch (uriMatcher.match(uri)) {
		case -1:
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
        return type;
	}

}
