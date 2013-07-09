package cz.ictsystem.lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import cz.ictsystem.stickers.R;

public class GalleryDateBackground {
	private static int mWidth;
	private static int mHeight;
	
	private Drawable mDrawable;
	
	public GalleryDateBackground(Context context, int mainColor, int triangelColor) {
			mWidth = context.getResources().getDimensionPixelSize(R.dimen.st_gallery_date_baground_width);
			mHeight = context.getResources().getDimensionPixelSize(R.dimen.st_gallery_title_baground_hight);

			Path main = new Path();
			main.lineTo(mWidth - (mHeight/2), 0);
			main.lineTo(mWidth, mHeight/2);
			main.lineTo(mWidth, mHeight);
			main.lineTo(0, mHeight);
			main.lineTo(0, 0);
			main.close();
			Paint mainPaint = new Paint();
			mainPaint.setColor(mainColor);

			Path triangel = new Path();
			triangel.moveTo(mWidth - (mHeight/2), 0);
			triangel.lineTo(mWidth, mHeight/2);
			triangel.lineTo(mWidth, 0);
			triangel.close();
			Paint triangelPaint = new Paint();
			triangelPaint.setColor(triangelColor);
			
			Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			canvas.drawPath(main, mainPaint);
			canvas.drawPath(triangel, triangelPaint);
			mDrawable = new BitmapDrawable(context.getResources(), bitmap);
	    }

	    public Drawable get() {
	        return mDrawable;
	    }
}
