package cz.ictsystem.stickers;

public class Const {

	public static final String FEED_URL = "https://spreadsheets.google.com/feeds/worksheets/0Auy9E6-b8VKtdGt4c25uUkM5ME5Lc0llZWZ2SVYxR3c/public/basic";
	
	public static final int MAIN_ACTIVITY_FRAGMENT_ID_VISUALIZATION = 0;
	public static final int MAIN_ACTIVITY_FRAGMENT_ID_STICKER_FEATURED = 1;
	public static final int MAIN_ACTIVITY_FRAGMENT_ID_STICKER_POPULAR = 2;
	public static final int MAIN_ACTIVITY_FRAGMENT_ID_STICKER_NEW = 3;
	public static final int MAIN_ACTIVITY_FRAGMENT_ID_CATEGORY = 4;
	public static final int MAIN_ACTIVITY_FRAGMENT_ID_STICKER_OF_CATEGORY = 5;

	public static final int STICKERS_ACTIVITY_FRAGMENT_ID_STICKER_OF_CATEGORY = 0;
	public static final int STICKERS_ACTIVITY_FRAGMENT_ID_STICKER_FEATURED = 1;
	public static final int STICKERS_ACTIVITY_FRAGMENT_ID_STICKER_POPULAR = 2;
	public static final int STICKERS_ACTIVITY_FRAGMENT_ID_STICKER_NEW = 3;

	public static final int FRAGMENT_STICKER_OF_CATEGORY = 0;
	public static final int FRAGMENT_STICKER_FEATURED = 1;
	public static final int FRAGMENT_STICKER_POPULAR = 2;
	public static final int FRAGMENT_STICKER_NEW = 3;

	
	public static final String ARG_CONTENT_TYPE = "content_type";
    public static final String ARG_ID = "id";
    public static final String ARG_CHOICE = "choice";
    public static final String ARG_COLOR = "color";
    public static final String ARG_PHOTO_FILE_URI = "photo_filre_uri";
    public static final String ARG_NEW_VISUALIZATION_NAME = "new_visualization_name";
    public static final String ARG_COLOR_PALATTE_VISIBILITY = "color_palette_visibility";
    public static final String ARG_PHOTO_NEW_VISUALIZATION = "new_visualization";
    public static final String ARG_MAIL_TYPE = "mail_type";
    public static final String ARG_EMPTY_VISUALIZATION = "empty_visualization";
    
    public static final String SYNCHRO_PREFS_NAME = "SynchroPrefsFile";
    public static final String SYNCHRO_PREFS_FIRST_SYNCHRO = "FirstSynchro";
    
	public static final int MAIL_TYPE_ORDER = 0;
	public static final int MAIL_TYPE_QUESTION = 1;
	public static final String FILE_PREFIX = "visualization";
	public static final String FILE_SUFFIX_JPG = ".jpg";
	public static final String FILE_SUFFIX_PNG = ".png";

	public static final String STUP_PROVIDER_AUTHORITY = "cz.ictsystem.stickers.data.StubProvider";
	public static final String ACCOUNT_TYPE = "cz.ictsystem.stickers";
    public static final String ACCOUNT = "dummyaccount";
    public static final long SYNC_INTERVAL = 7*24*60*60;//interval in seconds
	
}
