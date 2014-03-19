package cz.ictsystem.lib;

import android.content.Context;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import cz.ictsystem.stickers.R;

public class GallerySendOrderBackground {
	private static Path mBackgraound;
	private static int mWidth;
	private static int mHeight;
	private ShapeDrawable mDrawable;
	public GallerySendOrderBackground(Context context, int color) {
			mWidth = context.getResources().getDimensionPixelSize(R.dimen.st_gallery_buy_width);
			mHeight = context.getResources().getDimensionPixelSize(R.dimen.st_gallery_title_baground_hight);
			mBackgraound = new Path();
			
			mBackgraound.lineTo(0, 0);
			mBackgraound.lineTo(mWidth, 0);
			mBackgraound.lineTo(mWidth, mHeight);
			
			
			mBackgraound.lineTo(mHeight/2, mHeight);
			mBackgraound.lineTo(mHeight/2, mHeight/2);
			
			
			mBackgraound.close();
	        mDrawable = new ShapeDrawable(new PathShape(mBackgraound, mWidth, mHeight));
	        mDrawable.getPaint().setColor(color);
	    }

	    public ShapeDrawable get() {
	        return mDrawable;
	    }
}
