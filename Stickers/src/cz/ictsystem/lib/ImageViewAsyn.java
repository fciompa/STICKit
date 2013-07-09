package cz.ictsystem.lib;

import java.lang.ref.WeakReference;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.widget.ImageView;
import cz.ictsystem.stickers.Utils;

public class ImageViewAsyn {
	protected int mId;
	protected LruCache<Integer, Bitmap> mImageCache;
	protected Point mPoint;
	protected WeakReference<ImageView> mImageView;
	protected SetImageViewAsynTask mSetImageViewAsynTask;
	
	class SetImageViewAsynTask extends AsyncTask<byte[], Void, Bitmap>{
		protected int mId;
		protected LruCache<Integer, Bitmap> mImageCache;
		protected ImageView mImageView;
		protected Point mPoint;
		
		SetImageViewAsynTask(int id, LruCache<Integer, Bitmap> imageCache, Point point, ImageView imageView){
			mId = id;
			mImageCache = imageCache;
			mImageView = imageView;
			mPoint = point;
		}
		@Override
		protected Bitmap doInBackground(byte[]... parmSticker) {
			Bitmap bitmap = null;
			if(!isCancelled() && parmSticker[0] != null){
				bitmap = Utils.getBitmapFromBlob(parmSticker[0], mPoint.x, mPoint.y);
				mImageCache.put(mId, bitmap);
			}
			return bitmap;
		}
		
		@Override
		protected void onPostExecute(Bitmap bitmap) {
	        mImageView.setImageBitmap(bitmap);
	    }		
	}

	public ImageViewAsyn(LruCache<Integer, Bitmap> imageCache, Point point, ImageView imageView) {
		mImageCache = imageCache;
		mPoint = point;
		mImageView = new WeakReference<ImageView>(imageView);
	}
	

	@SuppressLint("NewApi")
	public void load(int id, byte[] image) {
		final ImageView imageView = mImageView.get();
		if(imageView != null){
			Bitmap imageBitmap = mImageCache.get(id);
			if(imageBitmap != null){
				imageView.setImageBitmap(imageBitmap);
			} else {
				if (mSetImageViewAsynTask != null && 
						(mSetImageViewAsynTask.getStatus() == Status.PENDING || mSetImageViewAsynTask.getStatus() == Status.RUNNING)) {
					mSetImageViewAsynTask.cancel(false);
					mSetImageViewAsynTask = null;
				}
				
				mSetImageViewAsynTask = new SetImageViewAsynTask(id, mImageCache, mPoint, imageView);

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					mSetImageViewAsynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, image);
				} else {
					mSetImageViewAsynTask.execute(image);
				}
			}
		}
	}
}
