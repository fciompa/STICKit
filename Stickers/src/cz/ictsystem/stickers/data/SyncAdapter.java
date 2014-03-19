package cz.ictsystem.stickers.data;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.accounts.Account;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.common.io.ByteStreams;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;

import cz.ictsystem.stickers.ActivityMain;
import cz.ictsystem.stickers.ActivityStickerDetail;
import cz.ictsystem.stickers.Const;
import cz.ictsystem.stickers.R;
import cz.ictsystem.stickers.Utils;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
	
	private static final String TAG = "SyncAdapter";
	private static final int NOTIFICATION_ID = 1;
	

	private Context mContext;
	private NotificationCompat.Builder mBuilder;
	private NotificationManager mNotifyMgr;
	private int notificationNumber;
	private String mDescriptionMoreStickers;

	public SyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
        Log.i(TAG, "SyncAdapter");
		mContext = context;
	}
	
	public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
		super(context, autoInitialize, allowParallelSyncs);
        Log.i(TAG, "SyncAdapter");
		mContext = context;
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

//	 	android.os.Debug.waitForDebugger();
			
		 Log.i(TAG, "onPerformSync");
        try {
			SpreadsheetService service = new SpreadsheetService(mContext.getString(R.string.app_name));
			URL feedUrl= new URL (Const.FEED_URL);
			WorksheetFeed feed= service.getFeed (feedUrl, WorksheetFeed.class);
			List<WorksheetEntry> worksheets = feed.getEntries();
			for(WorksheetEntry worksheetEntry : worksheets){
				ListFeed listFeed = service.getFeed(worksheetEntry.getListFeedUrl(), ListFeed.class);
				if(worksheetEntry.getTitle().getPlainText().equals(mContext.getString(R.string.table_category))){
					importCategory(listFeed);
				} else if (worksheetEntry.getTitle().getPlainText().equals(mContext.getString(R.string.table_sticker))){
					importSticker(listFeed);
				} else if (worksheetEntry.getTitle().getPlainText().equals(mContext.getString(R.string.table_stickercategory))){
					importStickerCategory(listFeed);
				}
//				else if(worksheetEntry.getTitle().getPlainText().equals(context.getString(R.string.table_visualization))){
//					importVisualization(listFeed);
//				}
			}
			Utils.setFirstSynchroDone(mContext);		
        } catch (ServiceException e) {
        	Log.d(getClass().getSimpleName(), e.getMessage());
        } catch (IOException e) {
            Log.d(getClass().getSimpleName(), e.getMessage());
        } catch (RuntimeException e) {
        	Log.d(getClass().getSimpleName(), e.getMessage());
        }
        
        Log.i(TAG, "onPerformSync - end");
	}

	private void importCategory(ListFeed listFeed){
		ArrayList<Integer> updatedItems = new ArrayList<Integer>();
		ContentResolver contentResolver = mContext.getContentResolver();
        for (ListEntry entry : listFeed.getEntries()) {
        	ContentValues category = new Category(mContext, Utils.getElements(entry)).getCategory(mContext);
        	String id = category.getAsString(mContext.getString(R.string.column_id));
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
    		cursor.moveToFirst();
        	do{
        		String id = cursor.getString(cursor.getColumnIndex(mContext.getString(R.string.column_id)));
        		if(!updatedItems.contains(Integer.valueOf(id))){
        			contentResolver.delete(Uri.withAppendedPath(DbProvider.URI_CATEGORY, id), id, null);
        		}
        	} while (cursor.moveToNext());
    	}
    	cursor.close();
    	
	}

	private void importSticker(ListFeed listFeed){
		ArrayList<Integer> updatedItems = new ArrayList<Integer>();
    	ContentResolver contentResolver = mContext.getContentResolver();
		for (ListEntry entry : listFeed.getEntries()) {
        	Sticker sticker = new Sticker(mContext, Utils.getElements(entry));
        	
        	String id = String.valueOf(sticker.getId());
        	updatedItems.add(Integer.valueOf(id));
        	Cursor cursor = contentResolver.query(Uri.withAppendedPath(DbProvider.URI_STICKER, id), 
        			null, null, null, null); 
        	if(cursor.getCount() == 1){
        		contentResolver.update(Uri.withAppendedPath(DbProvider.URI_STICKER, id),  sticker.getSticker(mContext), null, null);
        	} else {
        		sticker.setImage(downloadImage(sticker.getUrl()));
        		contentResolver.insert(DbProvider.URI_STICKER,  sticker.getSticker(mContext));
        		if(!Utils.getFirstSynchro(mContext)){
            		notifyNewSticker(sticker);
        		}
        	}
        	cursor.close();
	    }

		Cursor cursor = contentResolver.query(DbProvider.URI_STICKER, null, null, null, null);
		if(cursor.getCount()>0){
			cursor.moveToFirst();
	    	do{
	    		String id = cursor.getString(cursor.getColumnIndex(mContext.getString(R.string.column_id)));
	    		if(!updatedItems.contains(Integer.valueOf(id))){
	    			contentResolver.delete(Uri.withAppendedPath(DbProvider.URI_STICKER, id), id, null);
	    		}
	    	} while (cursor.moveToNext());
		}
    	cursor.close();
    	
	}

	private void notifyNewSticker(Sticker sticker) {

		if(notificationNumber == 0){
			Intent resultIntent = new Intent(mContext, ActivityStickerDetail.class);
	        resultIntent.putExtra(Const.ARG_ID, sticker.getId());
	        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
			stackBuilder.addParentStack(ActivityStickerDetail.class);
			stackBuilder.addNextIntent(resultIntent);		
			
			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);		
//	        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);		
			
	        mBuilder = new NotificationCompat.Builder(mContext)
				    .setSmallIcon(R.drawable.ic_stat_notification)
				    .setLargeIcon(Utils.getBitmapFromBlob(sticker.getImage(), 50, 50))
				    .setContentTitle(getContext().getString(R.string.new_sticker) + " " + sticker.getName())
				    .setContentText(sticker.getDescription())		
				    .setContentIntent(resultPendingIntent)
				    .setAutoCancel(true);
	        
	        mNotifyMgr = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
	        ++notificationNumber;
	        mDescriptionMoreStickers = sticker.getName();
		} else {
			mDescriptionMoreStickers = mDescriptionMoreStickers + ", "+ sticker.getName();
			mBuilder
				.setNumber(++notificationNumber)
				.setContentText(mDescriptionMoreStickers);
	        
	        if(notificationNumber <= 2){
				Intent resultIntent = new Intent(mContext, ActivityMain.class);
		        resultIntent.putExtra(Const.ARG_ID, sticker.getId());
//		        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		        mBuilder
		        	.setContentIntent(resultPendingIntent)
		        	.setContentTitle(getContext().getString(R.string.new_stickers));
	        }
		}
        mNotifyMgr.notify(NOTIFICATION_ID, mBuilder.build());
	}

	private void importStickerCategory(ListFeed listFeed){
		ArrayList<Integer> updatedItems = new ArrayList<Integer>();
    	ContentResolver contentResolver = mContext.getContentResolver();
        for (ListEntry entry : listFeed.getEntries()) {
        	ContentValues stickerCategory = new StickerCategory(mContext, Utils.getElements(entry)).getStickerCategory(mContext);
        	String id = stickerCategory.getAsString(mContext.getString(R.string.column_id));
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
        	cursorSticker.moveToFirst();
        	do{
        		String stickerId = cursorSticker.getString(cursorSticker.getColumnIndex(mContext.getString(R.string.column_id)));
        		if(!updatedItems.contains(Integer.valueOf(stickerId))){
        			contentResolver.delete(Uri.withAppendedPath(DbProvider.URI_STICKER_CATEGORY, stickerId), stickerId, null);
        			Cursor cursorVisualization = contentResolver.query(DbProvider.URI_VISUALIZATION, null, null, null, null);
        			if(cursorVisualization.getCount() > 0){
            			do{
            				cursorVisualization.moveToFirst();
            				String visualizationStickerId = cursorVisualization.getString(cursorVisualization.getColumnIndex(mContext.getString(R.string.column_visualization_sticker_id)));
            				if(visualizationStickerId != null && visualizationStickerId.equals(stickerId)){
            					String vizualizationId = cursorVisualization.getString(cursorVisualization.getColumnIndex(mContext.getString(R.string.column_id)));
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

//	private void importVisualization(ListFeed listFeed){
//		if(Utils.getFirstSynchro(mContext)){
//	        for (ListEntry entry : listFeed.getEntries()) {
//	        	Visualization visualization = new Visualization(mContext, Utils.getElements(entry));
//	        	visualization.setBackground(downloadImage(visualization.getUrl()));
//	        	visualization.setImage(
//	        			new VisualizationBuilder(
//	        					mContext, 
//	        					Utils.getDisplaySize(mContext), 
//	        					visualization).get());
//	        	visualization.save(mContext);
//		    }
//		}
//	}

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
