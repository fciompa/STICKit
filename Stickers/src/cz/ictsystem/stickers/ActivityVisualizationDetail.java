package cz.ictsystem.stickers;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class ActivityVisualizationDetail extends SherlockFragmentActivity {
	
	private static final String FRAGMENT_TAG = "VisualizationDetailFragment";
	
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
			
			VisualizationDetailFragment visualizationDetailFragment = new VisualizationDetailFragment();
			visualizationDetailFragment.setArguments(args);
			getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.fragment_container, visualizationDetailFragment, FRAGMENT_TAG)
				.commit();		
		}
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        boolean menuConsumed = false;
    	switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            menuConsumed = true;
            break;
        default:
        	menuConsumed = super.onMenuItemSelected(featureId, item); 
            break;
        }
        return menuConsumed;
    }
    
    @Override
    public void onBackPressed() {
    	setResult(RESULT_OK, getIntent());
    	finish();
    }
}
