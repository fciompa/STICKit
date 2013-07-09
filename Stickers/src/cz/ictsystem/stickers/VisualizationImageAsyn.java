package cz.ictsystem.stickers;

import java.lang.ref.WeakReference;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Build;
import android.widget.ImageView;

public class VisualizationImageAsyn {
	
	WeakReference<ImageView> mImageView;
	SetImageViewAsynTask mSetImageViewAsynTask;
	
	private class SetImageViewAsynTask extends AsyncTask<VisualizationBuilder, Void, Bitmap>{
		protected WeakReference<ImageView> mImageView;
		
		SetImageViewAsynTask(WeakReference<ImageView> imageView){
			super();
			mImageView = imageView;
		}
		
		@Override
		protected Bitmap doInBackground(VisualizationBuilder... parm) {
			VisualizationBuilder visualizationBuilder = parm[0];
			Bitmap bitmap = null;
			
			if (!isCancelled()) {
				bitmap = visualizationBuilder.get(); 
			}
			return bitmap;
		}
		
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (!isCancelled()) {
				if(mImageView != null){
					final ImageView imageView = mImageView.get();
					if(imageView != null){
						imageView.setImageBitmap(bitmap);
					}
				}
			}
	    }		
	}

	VisualizationImageAsyn(ImageView imageView){
		mImageView = new WeakReference<ImageView>(imageView);
	}
	
	@SuppressLint("NewApi")
	public void load(VisualizationBuilder visualizationBuilder) {
		final ImageView imageView = mImageView.get();
		if(imageView != null){
			if (mSetImageViewAsynTask != null && 
					(mSetImageViewAsynTask.getStatus() == Status.PENDING || mSetImageViewAsynTask.getStatus() == Status.RUNNING)) {
				mSetImageViewAsynTask.cancel(false);
				mSetImageViewAsynTask = null;
			}
			
			mSetImageViewAsynTask = new SetImageViewAsynTask(mImageView);

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				mSetImageViewAsynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, visualizationBuilder);
			} else {
				mSetImageViewAsynTask.execute(visualizationBuilder);
			}
		}
	}
}
