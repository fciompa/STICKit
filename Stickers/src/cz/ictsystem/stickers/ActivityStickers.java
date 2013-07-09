package cz.ictsystem.stickers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

/**
 * Activity contains list of stickers. Stickers can by filtered by category. Activity
 * can by use for browsing through stickers and their details, or activity can be used
 * for choosing of sticker. 
 * 
 * @author frantisek.ciompa
 *
 */
public class ActivityStickers extends SherlockFragmentActivity {
	
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setIcon(R.drawable.logo);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());        
        Utils.setPager(this, mSectionsPagerAdapter, mViewPager);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	boolean menuConsumed = false;
        switch (item.getItemId()) {
        	case android.R.id.home:
        		finish();
        		menuConsumed = true;
            default:
            	menuConsumed = super.onMenuItemSelected(featureId, item);
                break;
        }
        return menuConsumed; 
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

    	public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int fragmentId) {
        	Fragment fragment = null;
    		Bundle args = new Bundle();
    		args.putLong(Const.ARG_ID, getIntent().getExtras().getLong(Const.ARG_ID));
    		switch (fragmentId) {
            	case Const.STICKERS_ACTIVITY_FRAGMENT_ID_STICKER_FEATURED:
            		fragment = new StickersFragment();
            		args.putInt(Const.ARG_CONTENT_TYPE, Const.FRAGMENT_STICKER_FEATURED);
            		fragment.setArguments(args);
            		break;
            	
            	case Const.STICKERS_ACTIVITY_FRAGMENT_ID_STICKER_POPULAR:
            		fragment = new StickersFragment();
            		args.putInt(Const.ARG_CONTENT_TYPE, Const.FRAGMENT_STICKER_POPULAR);
            		fragment.setArguments(args);
            		break;
            		
            	case Const.STICKERS_ACTIVITY_FRAGMENT_ID_STICKER_NEW:
            		fragment = new StickersFragment();
            		args.putInt(Const.ARG_CONTENT_TYPE, Const.FRAGMENT_STICKER_NEW);
            		fragment.setArguments(args);
            		break;
            
            	case Const.STICKERS_ACTIVITY_FRAGMENT_ID_STICKER_OF_CATEGORY:
            		fragment = new StickersFragment();
            		args.putInt(Const.ARG_CONTENT_TYPE, Const.FRAGMENT_STICKER_OF_CATEGORY);
            		fragment.setArguments(args);
            		break;
            }
        	return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int fragmentId) {
        	String title = "";
            switch (fragmentId) {
                case Const.STICKERS_ACTIVITY_FRAGMENT_ID_STICKER_FEATURED: 
                	title = getString(R.string.page_title_featured);
                	break;
                case Const.STICKERS_ACTIVITY_FRAGMENT_ID_STICKER_POPULAR: 
                	title = getString(R.string.page_title_popular);
                	break;
                case Const.STICKERS_ACTIVITY_FRAGMENT_ID_STICKER_NEW: 
                	title = getString(R.string.page_title_new);
                	break;
                case Const.STICKERS_ACTIVITY_FRAGMENT_ID_STICKER_OF_CATEGORY: 
                	title = getString(R.string.page_title_all);
                	break;
            }
            return title;
        }
    }
}
