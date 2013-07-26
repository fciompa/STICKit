package cz.ictsystem.stickers.data;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
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
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;

import cz.ictsystem.stickers.R;
import cz.ictsystem.stickers.Utils;
import cz.ictsystem.stickers.Visualization;
import cz.ictsystem.stickers.VisualizationBuilder;

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
			URL feedUrl= new URL ("https://spreadsheets.google.com/feeds/worksheets/0Auy9E6-b8VKtdGt4c25uUkM5ME5Lc0llZWZ2SVYxR3c/public/basic");
			WorksheetFeed feed= service.getFeed (feedUrl, WorksheetFeed.class);
			List<WorksheetEntry> worksheets = feed.getEntries();
			for(WorksheetEntry worksheetEntry : worksheets){
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
			Utils.setFirstSynchroDone(getApplicationContext());		
        } catch (ServiceException e) {
        	Log.d(getClass().getSimpleName(), e.getMessage());
        } catch (IOException e) {
            Log.d(getClass().getSimpleName(), e.getMessage());
        }		
	}

	private void importCategory(ListFeed listFeed){
		ArrayList<Integer> updatedItems = new ArrayList<Integer>();
		ContentResolver contentResolver = getContentResolver();
        for (ListEntry entry : listFeed.getEntries()) {
        	ContentValues category = new Category(getApplicationContext(), Utils.getElements(entry)).getCategory(getApplicationContext());
        	String id = category.getAsString(getApplicationContext().getString(R.string.column_id));
        	updatedItems.add(Integer.valueOf(id));
        	Cursor cursor = contentResolver.query(Uri.withAppendedPath(DbProvider.URI_CATEGORY, id), 
        			null, null, null, null); 
        	if(cursor.getCount() == 1){
        		contentResolver.update(Uri.withAppendedPath(DbProvider.URI_CATEGORY, id), category, null, null);
        	} else {
        		contentResolver.insert(DbProvider.URI_CATEGORY, category);	
        	}
        	cursor.close();
	    }
        
    	Cursor cursor = contentResolver.query(DbProvider.URI_CATEGORY, null, null, null, null);
    	if(cursor.getCount()>0){
        	do{
        		String id = cursor.getString(cursor.getColumnIndex(getApplicationContext().getString(R.string.column_id)));
        		if(!updatedItems.contains(Integer.valueOf(id))){
        			contentResolver.delete(Uri.withAppendedPath(DbProvider.URI_CATEGORY, id), id, null);
        		}
        	} while (cursor.moveToNext());
    	}
    	cursor.close();
    	
	}

	private void importSticker(ListFeed listFeed){
		ArrayList<Integer> updatedItems = new ArrayList<Integer>();
    	ContentResolver contentResolver = getContentResolver();
		for (ListEntry entry : listFeed.getEntries()) {
        	Sticker sticker = new Sticker(getApplicationContext(), Utils.getElements(entry));
        	sticker.setImage(downloadImage(sticker.getUrl()));
        	String id = String.valueOf(sticker.getId());
        	updatedItems.add(Integer.valueOf(id));
        	Cursor cursor = contentResolver.query(Uri.withAppendedPath(DbProvider.URI_STICKER, id), 
        			null, null, null, null); 
        	if(cursor.getCount() == 1){
        		contentResolver.update(Uri.withAppendedPath(DbProvider.URI_STICKER, id),  sticker.getSticker(getApplicationContext()), null, null);
        	} else {
        		contentResolver.insert(DbProvider.URI_STICKER,  sticker.getSticker(getApplicationContext()));	
        	}
        	cursor.close();
	    }

		Cursor cursor = contentResolver.query(DbProvider.URI_STICKER, null, null, null, null);
		if(cursor.getCount()>0){
	    	do{
	    		String id = cursor.getString(cursor.getColumnIndex(getApplicationContext().getString(R.string.column_id)));
	    		if(!updatedItems.contains(Integer.valueOf(id))){
	    			contentResolver.delete(Uri.withAppendedPath(DbProvider.URI_STICKER, id), id, null);
	    		}
	    	} while (cursor.moveToNext());
		}
    	cursor.close();
    	
	}

	private void importStickerCategory(ListFeed listFeed){
		ArrayList<Integer> updatedItems = new ArrayList<Integer>();
    	ContentResolver contentResolver = getContentResolver();
        for (ListEntry entry : listFeed.getEntries()) {
        	ContentValues stickerCategory = new StickerCategory(getApplicationContext(), Utils.getElements(entry)).getStickerCategory(getApplicationContext());
        	String id = stickerCategory.getAsString(getApplicationContext().getString(R.string.column_id));
        	updatedItems.add(Integer.valueOf(id));
        	Cursor cursor = contentResolver.query(Uri.withAppendedPath(DbProvider.URI_STICKER_CATEGORY, id), 
        			null, null, null, null); 
        	if(cursor.getCount() == 1){
        		contentResolver.update(Uri.withAppendedPath(DbProvider.URI_STICKER_CATEGORY, id), stickerCategory, null, null);
        	} else {
        		contentResolver.insert(DbProvider.URI_STICKER_CATEGORY, stickerCategory);	
        	}
        	cursor.close();
	    }

        Cursor cursorSticker = contentResolver.query(DbProvider.URI_STICKER_CATEGORY, null, null, null, null);
        if(cursorSticker.getCount()>0){
        	do{
        		String stickerId = cursorSticker.getString(cursorSticker.getColumnIndex(getApplicationContext().getString(R.string.column_id)));
        		if(!updatedItems.contains(Integer.valueOf(stickerId))){
        			contentResolver.delete(Uri.withAppendedPath(DbProvider.URI_STICKER_CATEGORY, stickerId), stickerId, null);
        			Cursor cursorVisualization = contentResolver.query(DbProvider.URI_VISUALIZATION, null, null, null, null);
        			if(cursorVisualization.getCount() > 0){
            			do{
            				String visualizationStickerId = cursorVisualization.getString(cursorVisualization.getColumnIndex(getApplicationContext().getString(R.string.column_visualization_sticker_id)));
            				if(visualizationStickerId != null && visualizationStickerId.equals(stickerId)){
            					String vizualizationId = cursorVisualization.getString(cursorVisualization.getColumnIndex(getApplicationContext().getString(R.string.column_id)));
            					contentResolver.delete(Uri.withAppendedPath(DbProvider.URI_VISUALIZATION, vizualizationId), vizualizationId, null);
            				}
            			} while (cursorVisualization.moveToNext());
        			}
        			cursorVisualization.close();
        		}
        	} while (cursorSticker.moveToNext());
        }
    	cursorSticker.close();
	}

	private void importVisualization(ListFeed listFeed){
		if(Utils.getFirstSynchro(getApplicationContext())){
	        for (ListEntry entry : listFeed.getEntries()) {
	        	Visualization visualization = new Visualization(getApplicationContext(), Utils.getElements(entry));
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
