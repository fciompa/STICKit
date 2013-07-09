package cz.ictsystem.lib;

import android.content.Context;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import cz.ictsystem.stickers.R;

public class GalleryTitleBackground {
	private static Path mBackgraound;
	private static int mWidth;
	private static int mHeight;
	private ShapeDrawable mDrawable;
	public GalleryTitleBackground(Context context, int color) {
			mWidth = context.getResources().getDimensionPixelSize(R.dimen.st_gallery_title_baground_width);
			mHeight = context.getResources().getDimensionPixelSize(R.dimen.st_gallery_title_baground_hight);
			mBackgraound = new Path();
			mBackgraound.lineTo(mWidth - (mHeight/2), 0);
			mBackgraound.lineTo(mWidth, mHeight/2);
			mBackgraound.lineTo(mWidth, mHeight);
			mBackgraound.lineTo(0, mHeight);
			mBackgraound.lineTo(0, 0);
			mBackgraound.close();
	        mDrawable = new ShapeDrawable(new PathShape(mBackgraound, mWidth, mHeight));
	        mDrawable.getPaint().setColor(color);
	    }

	    public ShapeDrawable get() {
	        return mDrawable;
	    }
}
