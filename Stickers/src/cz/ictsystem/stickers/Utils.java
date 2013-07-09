package cz.ictsystem.stickers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.Display;
import android.view.WindowManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.IndicatorStyle;

import cz.ictsystem.stickers.data.Sticker;
import cz.ictsystem.stickers.data.User;

public class Utils {
	
	public static LruCache<Integer, Visualization> mVisualizationCache = 
			new LruCache<Integer, Visualization>((int) (Runtime.getRuntime().maxMemory() / 1024)/4);
	public static LruCache<Integer, Bitmap> mVisualizationImageCache = 
			new LruCache<Integer, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 1024)/4);
	public static LruCache<Integer, Bitmap> mStickerImageCache = 
			new LruCache<Integer, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 1024)/4);
	
    public static Bitmap getBitmapFromBlob(byte[] blob, int reqWidth, int reqHeight) {
		Bitmap bitmap = null;
		if (blob != null){
		    // First decode with inJustDecodeBounds=true to check dimensions
		    final BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeByteArray(blob, 0, blob.length, options);
		    
		    // Calculate inSampleSize
		    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	
		    // Decode bitmap with inSampleSize set
		    options.inJustDecodeBounds = false;
		    options.inPurgeable = true;
		    
			bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length, options);
		}
		return bitmap;
	}
    
    public static Bitmap getBitmapFromFile(String imagePath, int reqWidth, int reqHeight) {
		Bitmap bitmap = null;
		if (imagePath != null){
		    // First decode with inJustDecodeBounds=true to check dimensions
		    final BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeFile(imagePath, options);
		    
		    // Calculate inSampleSize
		    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	
		    // Decode bitmap with inSampleSize set
		    options.inJustDecodeBounds = false;
		    options.inPurgeable = true;
			bitmap = BitmapFactory.decodeFile(imagePath, options);
		}
		return bitmap;
	}
    
	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	    if (height > reqHeight || width > reqWidth) {
	        if (width > height) {
	            inSampleSize = Math.round((float)height / (float)reqHeight);
	        } else {
	            inSampleSize = Math.round((float)width / (float)reqWidth);
	        }
	    }
	    return inSampleSize;
	}			
    
	public static boolean isIntentAvailable(String action, Context context) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

	public static byte[] getBlobFromPNG(Bitmap bitmap) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100, bos);
		return bos.toByteArray();
	}

	public static byte[] getBlobJPEG(Bitmap bitmap){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		return stream.toByteArray();
	}
	
	public static Point getDisplaySize(Context context){
		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Point size = new Point();
		Point displaySize = getDisplaySize(display);
		if(displaySize.x >= displaySize.y){
			size.set(displaySize.x, displaySize.y);
		} else {
			size.set(displaySize.y, displaySize.x);
		}
		
		//recalculate to 3 by 2
		if(size.x/3 >= size.y/2){
			size.x = size.y/2*3;
		} else {
			size.y = size.x/3*2;
		}
		
		return size;
	}

	public static boolean getLandscape(Context context){
		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Point displaySize = getDisplaySize(display);
		return displaySize.x >= displaySize.y;
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private static Point getDisplaySize(Display display) {
		Point displaySize = new Point();
        if (android.os.Build.VERSION.SDK_INT >= 13) {
        	display.getSize(displaySize);
        } else {
        	displaySize.set(display.getWidth(), display.getHeight());
        }
		return displaySize;
	}
	
	public static boolean getFirstSynchro(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(Const.SYNCHRO_PREFS_NAME, 0);
		return preferences.getBoolean(Const.SYNCHRO_PREFS_FIRST_SYNCHRO, true);
	}
	
	public static void setFirstSynchroDone(Context context){
		SharedPreferences preferences = context.getSharedPreferences(Const.SYNCHRO_PREFS_NAME, 0);
		preferences.edit().putBoolean(Const.SYNCHRO_PREFS_FIRST_SYNCHRO, false).commit();
		
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void setPager(SherlockFragmentActivity sherlockFragmentActivity, FragmentPagerAdapter mSectionsPagerAdapter, ViewPager mViewPager) {
        mViewPager = (ViewPager) sherlockFragmentActivity.findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        
        TitlePageIndicator indicator = (TitlePageIndicator)sherlockFragmentActivity.findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);

        GradientDrawable drawable = new GradientDrawable();
        
        int[] colors = {
        		sherlockFragmentActivity.getResources().getColor(R.color.st_title_page_indicator_up), 
        		sherlockFragmentActivity.getResources().getColor(R.color.st_title_page_indicator_down)};
        
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            drawable.setColors(colors);        
            indicator.setBackground(drawable);
        } else {
            // Fallback for APIs under 16.
            GradientDrawable ngd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
            indicator.setBackgroundDrawable(ngd);
        }        

        final float density = sherlockFragmentActivity.getResources().getDisplayMetrics().density;
        indicator.setFooterColor(Color.WHITE);
        indicator.setFooterLineHeight(1 * density); //1dp
        indicator.setFooterIndicatorHeight(3 * density); //3dp
        indicator.setFooterIndicatorStyle(IndicatorStyle.Underline);
//		Modification drawing of IndicatorStyle.Underline in TitlePageIndicator:onDraw(Canvas canvas), because of adding of triangle
//      mPath.reset();
//      mPath.moveTo(leftMinusPadding, heightMinusLine);
//      mPath.lineTo(rightPlusPadding, heightMinusLine);
//      mPath.lineTo(rightPlusPadding, heightMinusLineMinusIndicator);
//      mPath.lineTo(leftMinusPadding + 3 * footerIndicatorLineHeight, heightMinusLineMinusIndicator);
//      mPath.lineTo(leftMinusPadding, heightMinusLineMinusIndicator - 3 * footerIndicatorLineHeight);
//      mPath.close();
        indicator.setTextColor(sherlockFragmentActivity.getResources().getColor(R.color.st_title_page_indicator_text_color));
        indicator.setSelectedColor(sherlockFragmentActivity.getResources().getColor(R.color.st_title_page_indicator_text_color));
        indicator.setSelectedBold(true);
        
	}
	public static void sendMail(Context context, Sticker mSticker, int color, User user, int mailType) {
		String chooserTitle = "";
		String body = "";
		String subject = "";
		String userInfo = context.getString(R.string.mail_user_info)
				.replace("@name", user.getName())
				.replace("@surname", user.getSurname())
				.replace("@firm", user.getFirm())
				.replace("@address", user.getAddress())
				.replace("@zipCode", user.getZipCode())
				.replace("@city", user.getCity())
				.replace("@phone", user.getPhone())
				.replace("@email", user.getEmail());

		switch (mailType) {
			case Const.MAIL_TYPE_ORDER:
				chooserTitle = context.getString(R.string.mail_order_chooser_title);
				subject = context.getString(R.string.mail_order_subject) + mSticker.getName();
				body = context.getString(R.string.mail_order_body).replace("@Color", String.valueOf(color)).replace("@UserInfo", userInfo);
				break;
				
			case Const.MAIL_TYPE_QUESTION:
				chooserTitle = context.getString(R.string.mail_question_chooser_title);
				subject = context.getString(R.string.mail_question_subject) + mSticker.getName();
				body = context.getString(R.string.mail_question_body).replace("@UserInfo", userInfo);
				break;
	
			default:
				throw new RuntimeException("Unknown mail type");
		}
		

		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SENDTO, Uri.parse("mailto:"));
//		emailIntent.setType("text/html");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String []{context.getString(R.string.mail_send_to)});
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(body));
		context.startActivity(Intent.createChooser(emailIntent, chooserTitle));
	}

	public static void shareBitmap(Context context, Bitmap bitmap, Bitmap.CompressFormat compressFormat) {
		try {
			File file = File.createTempFile(
					Const.FILE_PREFIX, 
					compressFormat == Bitmap.CompressFormat.JPEG ? Const.FILE_SUFFIX_JPG : Const.FILE_SUFFIX_PNG, 
					context.getApplicationContext().getExternalCacheDir());
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			bitmap.compress(compressFormat, 60, fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("image/*");
			intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
			context.startActivity(Intent.createChooser(intent, context.getString(R.string.sticker_share_image)));			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isCZ() {
		return Locale.getDefault().getDisplayLanguage().equals("èeština");
	}
	
}
