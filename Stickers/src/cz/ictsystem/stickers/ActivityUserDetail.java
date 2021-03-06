package cz.ictsystem.stickers;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.analytics.tracking.android.EasyTracker;

public class ActivityUserDetail extends SherlockFragmentActivity {

	private static final String FRAGMENT_TAG = "UserDetailFragment";

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setIcon(R.drawable.logo);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
		if(getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG)  == null){
    		UserDetailFragment userDetailFragment = new UserDetailFragment();
    		getSupportFragmentManager()
    			.beginTransaction()
    			.add(R.id.fragment_container, userDetailFragment, FRAGMENT_TAG)
    			.commit();		
        }
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
}
