package cz.ictsystem.stickers;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import cz.ictsystem.stickers.data.DbProvider;
import cz.ictsystem.stickers.data.SyncService;


/**
 * Main activity of application. Activity contains list of visualizations and
 * stickers. Lists of stickers are by type a categories of stickers.
 * 
 * @author frantisek.ciompa
 *
 */
public class ActivityMain extends SherlockFragmentActivity {
	
	final private static int REQUEST_CODE_NEW_ACTIVITY = 1;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setIcon(R.drawable.logo);
        getSupportActionBar().setTitle(null);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());        
        Utils.setPager(this, mSectionsPagerAdapter, mViewPager);
        synchronize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
        	case R.id.menu_new_visualization:
        		onNewVisualization();
        		break;
            case R.id.menu_sync:
            	synchronizationByUser();
                break;
            case R.id.menu_user_detail:
            	onOpenUserDetail();
                break;
            default:
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

	private void onOpenUserDetail() {
    	Intent myIntent = new Intent();
    	myIntent.setClass(this, ActivityUserDetail.class);
		startActivity(myIntent);
	}

	private void synchronize() {
		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
         
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null && 
        		!Utils.getFirstSynchro(this) && 
        		activeNetwork.isConnected() && 
        		activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
        	startService(new Intent(getApplicationContext(), SyncService.class));
        }
	}

	private void synchronizationByUser() {
		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if(activeNetwork != null && 
				!Utils.getFirstSynchro(this) && 
				activeNetwork.isConnected()){
			startService(new Intent(getApplicationContext(), SyncService.class));
		}
	}
    
    private void onNewVisualization(){
    	Visualization newVisualization = new Visualization(this);
    	newVisualization.save(this);
		Intent intent = new Intent(this, ActivityVisualizationDetail.class);
		intent.putExtra(Const.ARG_ID, newVisualization.getId());
		startActivityForResult(intent, REQUEST_CODE_NEW_ACTIVITY);
    }

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == REQUEST_CODE_NEW_ACTIVITY){
    		String id = String.valueOf(data.getExtras().getInt(Const.ARG_ID));
    		Cursor cursor = getContentResolver().query(
    				Uri.withAppendedPath(DbProvider.URI_VISUALIZATION, id),
					null, null, null, null);
    		Visualization visualization = new Visualization(this, cursor);
    		cursor.close();
    		if(visualization.getBackground() == null){
    			visualization.delete(this);
    		}
    	}
	}
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int fragmentId) {
        	Fragment fragment = null;
    		Bundle args = new Bundle();
            switch (fragmentId) {
            	case Const.MAIN_ACTIVITY_FRAGMENT_ID_VISUALIZATION:
            		fragment = new VisualizationsFragment();
	            	break;

            	case Const.MAIN_ACTIVITY_FRAGMENT_ID_STICKER_FEATURED:
            		fragment = new StickersFragment();
            		args.putInt(Const.ARG_CONTENT_TYPE, Const.FRAGMENT_STICKER_FEATURED);
            		fragment.setArguments(args);
            		break;
            	
            	case Const.MAIN_ACTIVITY_FRAGMENT_ID_STICKER_POPULAR:
            		fragment = new StickersFragment();
            		args.putInt(Const.ARG_CONTENT_TYPE, Const.FRAGMENT_STICKER_POPULAR);
            		fragment.setArguments(args);
            		break;
            		
            	case Const.MAIN_ACTIVITY_FRAGMENT_ID_STICKER_NEW:
            		fragment = new StickersFragment();
            		args.putInt(Const.ARG_CONTENT_TYPE, Const.FRAGMENT_STICKER_NEW);
            		fragment.setArguments(args);
            		break;
            
            	case Const.MAIN_ACTIVITY_FRAGMENT_ID_CATEGORY:
            		fragment = new CategoriesFragment();
            		break;
            }
        	
        	return fragment;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int fragmentId) {
        	String title = "";
            switch (fragmentId) {
                case Const.MAIN_ACTIVITY_FRAGMENT_ID_VISUALIZATION:
                	title = getString(R.string.page_title_my_wall);
                	break;
                case Const.MAIN_ACTIVITY_FRAGMENT_ID_STICKER_FEATURED: 
                	title = getString(R.string.page_title_featured);
                	break;
                case Const.MAIN_ACTIVITY_FRAGMENT_ID_STICKER_POPULAR: 
                	title = getString(R.string.page_title_popular);
                	break;
                case Const.MAIN_ACTIVITY_FRAGMENT_ID_STICKER_NEW: 
                	title = getString(R.string.page_title_new);
                	break;
                case Const.MAIN_ACTIVITY_FRAGMENT_ID_CATEGORY: 
                	title = getString(R.string.page_title_categories);
                	break;
            }
            return title;
        }
    }
}
