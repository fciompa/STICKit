package cz.ictsystem.stickers;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.actionbarsherlock.app.SherlockFragment;

import cz.ictsystem.stickers.data.DbProvider;


/**
 * A fragment displays list of visualizations.
 */
public class StickersFragment extends SherlockFragment 
		implements OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    
	private final static int ACTIVITY_REQUEST_CODE_CHOICE_STICKER = 0;
	
	private GridView mGridView;
	private StickersAdapter mAdapter;
	private View mLoading;
	private Uri mUri;
	
	private static final int LOADER_ID = 0;
	
	public StickersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.grid_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        long categoryId = args.getLong(Const.ARG_ID);

    	switch (args.getInt(Const.ARG_CONTENT_TYPE)){
    	case Const.FRAGMENT_STICKER_FEATURED:
    		mUri = (categoryId != 0) ? 
    				Uri.withAppendedPath(DbProvider.URI_FEATURED_STICKERS_BY_CATEGORY, String.valueOf(categoryId)) :
    					DbProvider.URI_FEATURED_STICKER;
    		break;
    	case Const.FRAGMENT_STICKER_POPULAR:
    		mUri = (categoryId != 0) ? 
    				Uri.withAppendedPath(DbProvider.URI_POPULAR_STICKER_BY_CATEGORY, String.valueOf(categoryId)) :
    					DbProvider.URI_POPULAR_STICKER;
    		break;
    	case Const.FRAGMENT_STICKER_NEW:
    		mUri = (categoryId != 0) ? 
    				Uri.withAppendedPath(DbProvider.URI_NEW_STICKERS_BY_CATEGORY, String.valueOf(categoryId)) : 
        				DbProvider.URI_NEW_STICKER;
    		break;
    	case Const.FRAGMENT_STICKER_OF_CATEGORY:
    		mUri = (categoryId != 0) ? 
    				Uri.withAppendedPath(DbProvider.URI_STICKERS_BY_CATEGORY, String.valueOf(categoryId)) : 
        				DbProvider.URI_STICKER;
    		break;
    	default:
    		throw new IllegalArgumentException("Unknown ARG_CONTENT_TYPE " + savedInstanceState.getInt(Const.ARG_CONTENT_TYPE));
    	}

    	mLoading = getView().findViewById(R.id.loading);
    	mAdapter = new StickersAdapter(getActivity(), null);

    	mGridView = (GridView) getView().findViewById(R.id.gridView);
    	int numColumns = Utils.getLandscape(getActivity()) ? 4 : 2;
    	mGridView.setNumColumns(numColumns);
    	mGridView.setOnItemClickListener(this);
    	mGridView.setAdapter(mAdapter);
    	
    	getLoaderManager().initLoader(LOADER_ID, null, this);
    }
	
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		Intent intent = new Intent(getActivity().getApplicationContext(), ActivityStickerDetail.class);
		intent.putExtra(Const.ARG_ID, Long.valueOf(id).intValue());

        if(getActivity().getIntent().hasExtra(Const.ARG_CHOICE)){
        	intent.putExtra(Const.ARG_CHOICE, getActivity().getIntent().getExtras().getBoolean(Const.ARG_CHOICE));
        	startActivityForResult(intent, ACTIVITY_REQUEST_CODE_CHOICE_STICKER);
        } else {
        	startActivity(intent);
        }
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	if(requestCode == ACTIVITY_REQUEST_CODE_CHOICE_STICKER){
    		if(resultCode == Activity.RESULT_OK){
    			Intent resultIntent = new Intent();
    			resultIntent.putExtra(Const.ARG_ID, intent.getExtras().getInt(Const.ARG_ID));
            	
    			getActivity().setResult(Activity.RESULT_OK, resultIntent);
                getActivity().finish();
    		}
    	} else {
    		super.onActivityResult(requestCode, resultCode, intent);
    	}
    	
    }
    
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		mLoading.setVisibility(View.VISIBLE);
		return new CursorLoader(getActivity(), mUri, null, null, null, null);
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if(data.getCount() > 0){
			mAdapter.swapCursor(data);	
		}
		mLoading.setVisibility(View.GONE);
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
		mLoading.setVisibility(View.GONE);
	}
}
