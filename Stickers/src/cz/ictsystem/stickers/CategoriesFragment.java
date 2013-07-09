package cz.ictsystem.stickers;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

import cz.ictsystem.stickers.data.DbProvider;

public class CategoriesFragment extends SherlockFragment 
		implements OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

	private ListView mListView;
	private SimpleCursorAdapter mAdapter;
	private View mLoading;
	
	private static final int LOADER_ID = 0;
    public CategoriesFragment() {
    }

    public static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false); 
    }

    @SuppressWarnings("deprecation")
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	mLoading = getView().findViewById(R.id.loading);

    	mAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, null, 
    			new String[]{getString(Utils.isCZ() ? R.string.column_category_name_cz : R.string.column_category_name_eng)}, 
    			new int[]{android.R.id.text1});
    	
    	mListView = (ListView) getView().findViewById(R.id.listView);
    	mListView.setOnItemClickListener(this);
    	mListView.setAdapter(mAdapter);
    	getLoaderManager().initLoader(LOADER_ID, null, this);
    	
    }

	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		Intent intent = new Intent(getActivity().getApplicationContext(), ActivityStickers.class);
		intent.putExtra(Const.ARG_ID, id);
		startActivity(intent);
	}

	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		mLoading.setVisibility(View.VISIBLE);
		return new CursorLoader(getActivity(), DbProvider.URI_CATEGORY, null, null, null, null);
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if(data.getCount() > 0){
			mAdapter.swapCursor(data);	
		}
		mLoading.setVisibility(View.GONE);
	}

	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.swapCursor(null);
		mLoading.setVisibility(View.GONE);
	}

}
