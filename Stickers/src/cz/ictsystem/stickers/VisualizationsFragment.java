package cz.ictsystem.stickers;

import android.content.Intent;
import android.database.Cursor;
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
public class VisualizationsFragment extends SherlockFragment 
		implements OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    
	private GridView mGridView;
	private VisualizationsAdapter mAdapter;
	private View mLoading;
	
	private static final int LOADER_ID = 0;

	public VisualizationsFragment() {
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.grid_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	mLoading = getView().findViewById(R.id.loading);

    	mAdapter = new VisualizationsAdapter(getActivity(), null);
    	
    	mGridView = (GridView) getView().findViewById(R.id.gridView);
    	int numColumns = Utils.getLandscape(getActivity()) ? 2 : 1;  
    	mGridView.setNumColumns(numColumns);
    	mGridView.setOnItemClickListener(this);
    	mGridView.setAdapter(mAdapter);
    	getLoaderManager().initLoader(LOADER_ID, null, this);
    }
	
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		Intent intent = new Intent(getActivity().getApplicationContext(), ActivityVisualizationDetail.class);
		intent.putExtra(Const.ARG_ID, Long.valueOf(id).intValue());
		startActivity(intent);
	}


	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		mLoading.setVisibility(View.VISIBLE);
		return new CursorLoader(getActivity(), DbProvider.URI_VISUALIZATION, null, null, null, null);
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if(data.getCount() > 0){
			mAdapter.swapCursor(data);
			mLoading.setVisibility(View.GONE);
		} else {
			if(!Utils.getFirstSynchro(getActivity())){
				mLoading.setVisibility(View.GONE);
			}
		}
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
		mLoading.setVisibility(View.GONE);
	}
}
