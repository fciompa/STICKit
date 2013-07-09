package cz.ictsystem.stickers.data;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import com.google.common.io.ByteStreams;
import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.ParseSource;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;

import cz.ictsystem.stickers.R;
import cz.ictsystem.stickers.Utils;
import cz.ictsystem.stickers.VisualizationBuilder;
import cz.ictsystem.stickers.Visualization;

public class SyncService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Thread t = new Thread(""){
			@Override
			public void run(){
				syncData();
			}
		};
		t.start();
		return START_STICKY;
	}
	private void syncData(){
        Context context = getApplicationContext();
        try {
			SpreadsheetService service = new SpreadsheetService(context.getString(R.string.app_name));
//	    	service.setUserCredentials("frantisek.ciompa@gmail.com", "fc8729");
//			service.setUserCredentials("app.nalepshop@gmail.com", "nalep258shop");
			service.setUserCredentials(getString(R.string.web_user_login), getString(R.string.web_user_password));
	    	FeedURLFactory factory = FeedURLFactory.getDefault();
			SpreadsheetFeed feed = service.getFeed(factory.getSpreadsheetsFeedUrl(), SpreadsheetFeed.class);
	
			
			
//			URL feedUrl= new URL ("https://docs.google.com/spreadsheet/ccc?key=0ApDWMw1Qm1g_dFJKaTBBQkVUbzk1aE9CT1pXMHBoVXc&usp=sharing");
//			URL feedUrl= new URL ("https://docs.google.com/spreadsheet/pub?key=0ApDWMw1Qm1g_dFJKaTBBQkVUbzk1aE9CT1pXMHBoVXc");
			//https://spreadsheets.google.com/feeds/worksheets/0ApDWMw1Qm1g_dFJKaTBBQkVUbzk1aE9CT1pXMHBoVXc/public/basic
			//https://spreadsheets.google.com/feeds/worksheets/key/private/basic
//			URL feedUrl= new URL ("https://spreadsheets.google.com/feeds/worksheets/0ApDWMw1Qm1g_dFJKaTBBQkVUbzk1aE9CT1pXMHBoVXc/public/basic");
//			WorksheetFeed feed= service.getFeed (feedUrl, WorksheetFeed.class);
//			List<WorksheetEntry> worksheets = feed.getEntries();
//			for(WorksheetEntry worksheetEntry : worksheets){
//				ListFeed listFeed = service.getFeed(worksheetEntry.getListFeedUrl(), ListFeed.class);
//				if(worksheetEntry.getTitle().getPlainText().equals(context.getString(R.string.table_category))){
//					importCategory(listFeed);
//				} else if (worksheetEntry.getTitle().getPlainText().equals(context.getString(R.string.table_sticker))){
//					importSticker(listFeed);
//				} else if (worksheetEntry.getTitle().getPlainText().equals(context.getString(R.string.table_stickercategory))){
//					importStickerCategory(listFeed);
//				} else if(worksheetEntry.getTitle().getPlainText().equals(context.getString(R.string.table_visualization))){
//					importVisualization(listFeed);
//				}
//			}
			
			
			List<SpreadsheetEntry> spreadsheets = feed.getEntries();
			for (SpreadsheetEntry spreadsheet : spreadsheets) {
				if(spreadsheet.getTitle().getPlainText().equals(context.getString(R.string.app_name))){
					for(WorksheetEntry worksheetEntry : spreadsheet.getWorksheets()){
						ListFeed listFeed = service.getFeed(worksheetEntry.getListFeedUrl(), ListFeed.class);
						if(worksheetEntry.getTitle().getPlainText().equals(context.getString(R.string.table_category))){
							importCategory(listFeed);
						} else if (worksheetEntry.getTitle().getPlainText().equals(context.getString(R.string.table_sticker))){
							importSticker(listFeed);
						} else if (worksheetEntry.getTitle().getPlainText().equals(context.getString(R.string.table_stickercategory))){
							importStickerCategory(listFeed);
						} else if(worksheetEntry.getTitle().getPlainText().equals(context.getString(R.string.table_visualization))){
							importVisualization(listFeed);
						}
					}
					break;
				}
			}
			Utils.setFirstSynchroDone(getApplicationContext());		
        } catch (ServiceException e) {
			e.printStackTrace();
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "Drive esplode", e);
        }		
		
	}

	private void importCategory(ListFeed listFeed){
        for (ListEntry entry : listFeed.getEntries()) {
        	ContentResolver contentResolver = getContentResolver();
        	ContentValues category = new Category(getApplicationContext(), entry).getCategory(getApplicationContext());
        	String id = category.getAsString(getApplicationContext().getString(R.string.column_id));
        	Cursor cursor = contentResolver.query(Uri.withAppendedPath(DbProvider.URI_CATEGORY, id), 
        			null, null, null, null); 
        	if(cursor.getCount() == 1){
        		contentResolver.update(Uri.withAppendedPath(DbProvider.URI_CATEGORY, id), category, null, null);
        	} else {
        		contentResolver.insert(DbProvider.URI_CATEGORY, category);	
        	}
        	cursor.close();
	    }
	}

	private void importSticker(ListFeed listFeed){
		for (ListEntry entry : listFeed.getEntries()) {
        	ContentResolver contentResolver = getContentResolver();
        	Sticker sticker = new Sticker(getApplicationContext(), entry);
        	sticker.setImage(downloadImage(sticker.getUrl()));
        	String id = String.valueOf(sticker.getId());
        	Cursor cursor = contentResolver.query(Uri.withAppendedPath(DbProvider.URI_STICKER, id), 
        			null, null, null, null); 
        	if(cursor.getCount() == 1){
        		contentResolver.update(Uri.withAppendedPath(DbProvider.URI_STICKER, id),  sticker.getSticker(getApplicationContext()), null, null);
        	} else {
        		contentResolver.insert(DbProvider.URI_STICKER,  sticker.getSticker(getApplicationContext()));	
        	}
        	cursor.close();
	    }
	}

	private void importStickerCategory(ListFeed listFeed){
        for (ListEntry entry : listFeed.getEntries()) {
        	ContentResolver contentResolver = getContentResolver();
        	ContentValues stickerCategory = new StickerCategory(getApplicationContext(), entry).getStickerCategory(getApplicationContext());
        	String id = stickerCategory.getAsString(getApplicationContext().getString(R.string.column_id));
        	Cursor cursor = contentResolver.query(Uri.withAppendedPath(DbProvider.URI_STICKER_CATEGORY, id), 
        			null, null, null, null); 
        	if(cursor.getCount() == 1){
        		contentResolver.update(Uri.withAppendedPath(DbProvider.URI_STICKER_CATEGORY, id), stickerCategory, null, null);
        	} else {
        		contentResolver.insert(DbProvider.URI_STICKER_CATEGORY, stickerCategory);	
        	}
        	cursor.close();
	    }
	}

	private void importVisualization(ListFeed listFeed){
		if(Utils.getFirstSynchro(getApplicationContext())){
	        for (ListEntry entry : listFeed.getEntries()) {
	        	Visualization visualization = new Visualization(getApplicationContext(), entry);
	        	visualization.setBackground(downloadImage(visualization.getUrl()));
	        	visualization.setImage(
	        			new VisualizationBuilder(
	        					getApplicationContext(), 
	        					Utils.getDisplaySize(getApplicationContext()), 
	        					visualization).get());
	        	visualization.save(getApplicationContext());
		    }
		}
	}

	private byte[] downloadImage(String myurl) {
	    InputStream is = null;
	    byte[] blob = null; 
	    try {
	        URL url = new URL(myurl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setReadTimeout(10000 /* milliseconds */);
	        conn.setConnectTimeout(15000 /* milliseconds */);
	        conn.setRequestMethod("GET");
	        conn.setDoInput(true);
	        conn.connect();
	        if (conn.getResponseCode() == 200){
		        is = conn.getInputStream();
		        blob = ByteStreams.toByteArray(is);
	        }
	    } catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
	        if (is != null) {
	            try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	        } 
	    }
	    return blob;
	}
}