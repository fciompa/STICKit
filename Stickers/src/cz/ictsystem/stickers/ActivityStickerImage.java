package cz.ictsystem.stickers;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;

import cz.ictsystem.lib.ImageViewAsyn;
import cz.ictsystem.stickers.data.DbProvider;
import cz.ictsystem.stickers.data.Sticker;

public class ActivityStickerImage extends SherlockFragmentActivity{

	private ImageView mStickerImage;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_image);
        
        getSupportActionBar().setIcon(R.drawable.logo);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        mStickerImage = (ImageView) findViewById(R.id.sticker_image);
        @SuppressWarnings("unused")
		PhotoViewAttacher attacher = new PhotoViewAttacher(mStickerImage);
        
        Cursor cursor = getContentResolver().query(
				Uri.withAppendedPath(DbProvider.URI_STICKER, String.valueOf(getIntent().getExtras().getInt(Const.ARG_ID))),
				null, null, null, null);
		Sticker mSticker = new Sticker(cursor);
        cursor.close();

        new ImageViewAsyn(
				Utils.mStickerImageCache, 
				Utils.getDisplaySize(this), 
				mStickerImage).load(mSticker.getId(), mSticker.getImage());
    }
	
	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean menuConsumed = false;
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            finish();
	            menuConsumed = true;
	            break;
	        default:
	        	menuConsumed = super.onOptionsItemSelected(item);
	            break;
	    }
	    return menuConsumed;
	}

}
