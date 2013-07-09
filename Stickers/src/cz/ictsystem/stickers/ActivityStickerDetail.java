package cz.ictsystem.stickers;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * Detail activity of sticker.
 * 
 * @author frantisek.ciompa
 *
 */
public class ActivityStickerDetail extends SherlockFragmentActivity {
	
	private static final String FRAGMENT_TAG = "StickerDetailFragment";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setIcon(R.drawable.logo);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
		if(getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG)  == null){
    		Bundle args = new Bundle();
    		args.putInt(Const.ARG_ID, getIntent().getExtras().getInt(Const.ARG_ID));
    		
    		StickerDetailFragment stickerDetailFragment = new StickerDetailFragment();
    		stickerDetailFragment.setArguments(args);
    		getSupportFragmentManager()
    			.beginTransaction()
    			.add(R.id.fragment_container, stickerDetailFragment, FRAGMENT_TAG)
    			.commit();		
        }
    }
}
