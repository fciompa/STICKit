package cz.ictsystem.stickers;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
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
import com.crashlytics.android.Crashlytics;
import com.google.analytics.tracking.android.EasyTracker;

import cz.ictsystem.lib.AboutDialog;
import cz.ictsystem.stickers.data.DbProvider;
import cz.ictsystem.stickers.data.User;


/**
 * Main activity of application. Activity contains list of visualizations and
 * stickers. Lists of stickers are by type a categories of stickers.
 * 
 * @author frantisek.ciompa
 *
 */
public class ActivityMain extends SherlockFragmentActivity {
	
//	final private static String TAG = "ActivityMain";
	private final static int ACTIVITY_REQUEST_CODE_NEW_ACTIVITY = 1;
	private final static int ACTIVITY_REGUEST_CODE_OPEN_USER_INFO = 4;
	
//	private final int DIALOG_ABOUT = 99;

	private Account  mAccount;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        saveDatabseToExternal();
        
        Crashlytics.start(this);
		setContentView(R.layout.activity_main);
        getSupportActionBar().setIcon(R.drawable.logo);
        getSupportActionBar().setTitle(null);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());        
        Utils.setPager(this, mSectionsPagerAdapter, mViewPager);
        setUpAndRunSynchronization();
    }
    
//    private void saveDatabseToExternal() {
//    	try {
//    		final String inFileName = "/data/data/cz.ictsystem.stickers/databases/nalepShop";
//    		File dbFile = new File(inFileName);
//    		FileInputStream fis = new FileInputStream(dbFile);
//
//    		String outFileName = Environment.getExternalStorageDirectory()+"/nalepShop_copy.db";
//
//    		Log.d(TAG, outFileName);
//    		
//    		// Open the empty db as the output stream
//    		OutputStream output = new FileOutputStream(outFileName);
//
//    		// Transfer bytes from the inputfile to the outputfile
//    		byte[] buffer = new byte[1024];
//    		int length;
//    		while ((length = fis.read(buffer))>0){
//    			output.write(buffer, 0, length);
//    		}
//
//    		// Close the streams
//    		output.flush();
//    		output.close();
//    		fis.close();	
//    	} catch (FileNotFoundException e) {
//    		// TODO Auto-generated catch block
//    		e.printStackTrace();
//    	} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }

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
            case R.id.menu_help:
            	onOpenHelp();
                break;
            case R.id.menu_terms_and_activity:
            	onOpenTermsAndActivity();
                break;
            case R.id.menu_about:
            	onAbout();
                break;
            default:
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

	private void onOpenHelp() {
    	Intent myIntent = new Intent();
    	myIntent.setClass(this, ActivityHelp.class);
		startActivity(myIntent);
	}
	
	private void onOpenTermsAndActivity() {
    	Intent myIntent = new Intent();
    	myIntent.setClass(this, ActivityTermsAndCondition.class);
		startActivity(myIntent);
	}

	private void onAbout() {
		AboutDialog about = new AboutDialog(this);
		about.setTitle(R.string.menu_about);
		about.show();
	}

	private void onOpenUserDetail() {
    	Intent myIntent = new Intent();
    	myIntent.setClass(this, ActivityUserDetail.class);
		startActivity(myIntent);
	}

	private void setUpAndRunSynchronization() {
        final String accountName = Const.ACCOUNT;
		mAccount = new Account(accountName, Const.ACCOUNT_TYPE);
        AccountManager accountManager = (AccountManager) getApplicationContext().getSystemService(ACCOUNT_SERVICE);
        if (accountManager.addAccountExplicitly(mAccount, null, null)) {
        	ContentResolver.setIsSyncable(mAccount, Const.STUP_PROVIDER_AUTHORITY, 1);
    		ContentResolver.setSyncAutomatically(mAccount, Const.STUP_PROVIDER_AUTHORITY, true);		
    		ContentResolver.addPeriodicSync(mAccount, Const.STUP_PROVIDER_AUTHORITY, new Bundle(), Const.SYNC_INTERVAL);
        }

		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
         
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null && 
        		activeNetwork.isConnected()){
        	if (Utils.getFirstSynchro(this) || activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                requestSynchronization();
        	}
        }
	}

	private void synchronizationByUser() {
		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if(activeNetwork != null && activeNetwork.isConnected()){
			requestSynchronization();
		}
	}
    
	private void requestSynchronization() {
		Bundle b = new Bundle();
        b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(mAccount, Const.STUP_PROVIDER_AUTHORITY, b);
	}

    public void onNewVisualization(){
    	Visualization newVisualization = new Visualization(this);
    	newVisualization.save(this);
		Intent intent = new Intent(this, ActivityVisualizationDetail.class);
		intent.putExtra(Const.ARG_ID, newVisualization.getId());
		startActivityForResult(intent, ACTIVITY_REQUEST_CODE_NEW_ACTIVITY);
    }

	public void onSendOrder(int mailType, int id) {
		User user = new User(getApplicationContext());
		if(user.isEmpty()){
			Intent intent = new Intent(this, ActivityUserDetail.class);
			intent.putExtra(Const.ARG_MAIL_TYPE, mailType);
			intent.putExtra(Const.ARG_ID, id);
			startActivityForResult(intent, ACTIVITY_REGUEST_CODE_OPEN_USER_INFO);
		} else {
        	Cursor cursor = getContentResolver().query(
    				Uri.withAppendedPath(DbProvider.URI_VISUALIZATION, String.valueOf(id)),
    				null, null, null, null);
        	
        	VisualizationBuilder builder = new VisualizationBuilder(
        			this, 
        			Utils.getDisplaySize(this), 
        			new Visualization(this, cursor));
    		cursor.close();
			
			Utils.sendMail(this, builder.getSticker(), builder.getVisualization().getColor(), user, mailType);
		}
	}

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == ACTIVITY_REQUEST_CODE_NEW_ACTIVITY){
    		if(data != null){
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
    	} else if (requestCode == ACTIVITY_REGUEST_CODE_OPEN_USER_INFO){
			if(resultCode == Activity.RESULT_OK){
				onSendOrder(data.getIntExtra(Const.ARG_MAIL_TYPE, Const.MAIL_TYPE_QUESTION), data.getIntExtra(Const.ARG_ID, 0));
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
