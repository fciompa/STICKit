package cz.ictsystem.stickers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.os.Bundle;
import android.webkit.WebView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;

public class ActivityHelp extends SherlockFragmentActivity {

//	private static final String FRAGMENT_TAG = "ActivityTermsAndCondition";

	private WebView webView;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        
        getSupportActionBar().setIcon(R.drawable.logo);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
		webView = (WebView) findViewById(R.id.webViewHelp);
		String data = "";
	    try {
			InputStream in_s = getResources().openRawResource(R.raw.help_cz);
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(in_s));
	    	String line = reader.readLine();
	    	while (line != null) {
	    		data = data + line;
	    		line = reader.readLine();
	    	}	    
	    } catch (Exception e) {
	        // e.printStackTrace();
	    	data = new String("Error: can't show Help.");
	    }		
		
		webView.loadData(data,  "text/html", "utf-8");
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
