package cz.ictsystem.stickers;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import cz.ictsystem.lib.GalleryTitleBackground;
import cz.ictsystem.lib.ImageViewAsyn;
import cz.ictsystem.stickers.data.DbProvider;
import cz.ictsystem.stickers.data.Sticker;
import cz.ictsystem.stickers.data.User;

public class StickerDetailFragment extends SherlockFragment 
		implements LoaderManager.LoaderCallbacks<Cursor>{

	private final static int LOADER_ID = 1;
	private final static int REGUEST_CODE_OPEN_USER_INFO = 1;
	final private static int REQUEST_CODE_NEW_ACTIVITY = 2;
	
	private TextView mName;
	private ImageView mImage;
	private TextView mDescription;
	private View mColorPalette;
	private View mEditColorView;
	private ImageButton mEditColor;
	private TextView mPriceCZK;
	private TextView mPriceEUR;
	private Button mMainButton;
	private TextView mCategory;
	private TextView mExpeditionTime;
	
	private ImageButton mColor_01;
	private ImageButton mColor_02;
	private ImageButton mColor_03;
	private ImageButton mColor_04;
	private ImageButton mColor_05;
	private ImageButton mColor_06;
	private ImageButton mColor_07;
	private ImageButton mColor_08;
	private ImageButton mColor_09;
	private ImageButton mColor_10;
	private ImageButton mColor_11;
	private ImageButton mColor_12;
	private ImageButton mColor_13;
	private ImageButton mColor_14;
	private ImageButton mColor_15;
	private ImageButton mColor_16;

	private int mStickerId;
	private Sticker mSticker;
	private int mColor;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sticker_detail, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	
    	if((savedInstanceState != null) && (savedInstanceState.containsKey(Const.ARG_COLOR))){
    		mColor  = savedInstanceState.getInt(Const.ARG_COLOR);
    	} else {
        	mColor = 0;
    	}
    	
    	setHasOptionsMenu(true);
    	mStickerId = getArguments().getInt(Const.ARG_ID);
    	
    	mName = (TextView) getView().findViewById(R.id.sticker_name);
    	mImage = (ImageView) getView().findViewById(R.id.sticker_image);
    	mDescription = (TextView) getView().findViewById(R.id.sticker_description);
    	mColorPalette = getView().findViewById(R.id.sticker_color_palette);
    	mEditColorView = getView().findViewById(R.id.sticker_edit_color_view);
    	mEditColor = (ImageButton) getView().findViewById(R.id.sticker_edit_color);
    	mPriceCZK  = (TextView) getView().findViewById(R.id.sticker_priceCZK);
    	mPriceEUR = (TextView) getView().findViewById(R.id.sticker_priceEUR);
    	mMainButton = (Button) getView().findViewById(R.id.sticker_main_button);
    	mCategory = (TextView) getView().findViewById(R.id.sticker_category);
    	mExpeditionTime = (TextView) getView().findViewById(R.id.sticker_expedition_time);
    	
    	mColor_01 = (ImageButton) getView().findViewById(R.id.color_01);
    	mColor_02 = (ImageButton) getView().findViewById(R.id.color_02);
    	mColor_03 = (ImageButton) getView().findViewById(R.id.color_03);
    	mColor_04 = (ImageButton) getView().findViewById(R.id.color_04);
    	mColor_05 = (ImageButton) getView().findViewById(R.id.color_05);
    	mColor_06 = (ImageButton) getView().findViewById(R.id.color_06);
    	mColor_07 = (ImageButton) getView().findViewById(R.id.color_07);
    	mColor_08 = (ImageButton) getView().findViewById(R.id.color_08);
    	mColor_09 = (ImageButton) getView().findViewById(R.id.color_09);
    	mColor_10 = (ImageButton) getView().findViewById(R.id.color_10);
    	mColor_11 = (ImageButton) getView().findViewById(R.id.color_11);
    	mColor_12 = (ImageButton) getView().findViewById(R.id.color_12);
    	mColor_13 = (ImageButton) getView().findViewById(R.id.color_13);
    	mColor_14 = (ImageButton) getView().findViewById(R.id.color_14);
    	mColor_15 = (ImageButton) getView().findViewById(R.id.color_15);
    	mColor_16 = (ImageButton) getView().findViewById(R.id.color_16);

    	mEditColor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mColorPalette.setVisibility(View.VISIBLE);
				mEditColorView.setVisibility(View.GONE);
			}
		});
    	
    	mMainButton.setBackgroundDrawable((new GalleryTitleBackground(
    			getActivity(), 
				getActivity().getResources().getColor(R.color.st_galery_title_background))).get());

    	if(isChoice()){
    		mMainButton.setText(R.string.sticker_text_main_button_choice);
        	mMainButton.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    		    	onOK();
    			}
    		});
    	} else {
    		mMainButton.setText(R.string.sticker_text_main_button_buy);
        	mMainButton.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				onSendMail(Const.MAIL_TYPE_ORDER);
    			}
    		});
    	}

    	mColor_01.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onChangeColor(R.color.color_01);
			}
		});

    	mColor_02.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onChangeColor(R.color.color_02);
			}

		});
    	
    	mColor_03.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onChangeColor(R.color.color_03);
			}

		});
    	
    	mColor_04.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onChangeColor(R.color.color_04);
			}

		});
    	
    	mColor_05.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onChangeColor(R.color.color_05);
			}

		});
    	
    	mColor_06.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onChangeColor(R.color.color_06);
			}

		});
    	
    	mColor_07.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onChangeColor(R.color.color_07);
			}

		});
    	
    	mColor_08.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onChangeColor(R.color.color_08);
			}

		});
    	
    	mColor_09.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onChangeColor(R.color.color_09);
			}

		});
    	
    	mColor_10.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onChangeColor(R.color.color_10);
			}

		});
    	
    	mColor_11.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onChangeColor(R.color.color_11);
			}

		});
    	
    	mColor_12.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onChangeColor(R.color.color_12);
			}

		});
    	
    	mColor_13.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onChangeColor(R.color.color_13);
			}

		});
    	
    	mColor_14.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onChangeColor(R.color.color_14);
			}

		});
    	
    	mColor_15.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onChangeColor(R.color.color_15);
			}

		});
    	
    	mColor_16.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onChangeColor(R.color.color_16);
			}

		});
    	
    	getLoaderManager().initLoader(LOADER_ID, null, this);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	inflater.inflate(R.menu.sticker_fragment, menu);
//        menu.findItem(R.id.menu_ok).setVisible(isChoice());
        menu.findItem(R.id.menu_new_visualization).setVisible(!isChoice());
        menu.findItem(R.id.menu_buy).setVisible(isChoice());
    }

	private boolean isChoice() {
		return getActivity().getIntent().getExtras().getBoolean(Const.ARG_CHOICE);
	}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	boolean menuConsumed = false;
        switch (item.getItemId()) {
	        case android.R.id.home:
	            getActivity().finish();
	            menuConsumed = true;
	            break;
	        case R.id.menu_new_visualization:
	        	onNewVisualization();
	        	menuConsumed = true;
	        	break;
	        case R.id.menu_buy:
	        	onSendMail(Const.MAIL_TYPE_ORDER);
	        	menuConsumed = true;
	        	break;
	        case R.id.menu_share:
	        	onShare();
	        	menuConsumed = true;
	        	break;
	        case R.id.menu_send_question:
	        	onSendMail(Const.MAIL_TYPE_QUESTION);
	        	menuConsumed = true;
	        	break;
	        default:
	        	menuConsumed = super.onOptionsItemSelected(item);
	            break;
        }
        return menuConsumed;
    }

	private void onShare() {
		Bitmap bitmap = Utils.getBitmapFromBlob(mSticker.getImage(), 800, 640); 
		Utils.shareBitmap(getActivity(), bitmap, Bitmap.CompressFormat.PNG);
	}

	private void onOK() {
		Intent intent = new Intent();
		intent.putExtra(Const.ARG_ID, mStickerId);
		getActivity().setResult(Activity.RESULT_OK, intent);
		getActivity().finish();
	}
	
	private void onNewVisualization(){
    	Visualization newVisualization = new Visualization(getActivity());
    	newVisualization.setStickerId(mSticker.getId()).setColor(mColor);
    	newVisualization.save(getActivity());
		Intent intent = new Intent(getActivity(), ActivityVisualizationDetail.class);
		intent.putExtra(Const.ARG_ID, newVisualization.getId());
		startActivityForResult(intent, REQUEST_CODE_NEW_ACTIVITY);
	}

	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return new CursorLoader(getActivity(), Uri.withAppendedPath(DbProvider.URI_STICKER, String.valueOf(mStickerId)), null, null, null, null);
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mSticker = new Sticker(cursor);

		mName.setText(mSticker.getName());
    	new ImageViewAsyn(Utils.mStickerImageCache, Utils.getDisplaySize(getActivity()), mImage).load(mSticker.getId(), mSticker.getImage());
		mDescription.setText(mSticker.getDescription());
    	mPriceCZK.setText(mSticker.getPriceCZK().toString());
    	mPriceEUR.setText(mSticker.getPriceEUR().toString());
    	mCategory.setText(buildCategory());
    	mExpeditionTime.setText(mSticker.getExpeditionTime() + getString(R.string.sticker_text_expedition_time_unit_week));

    	if(mSticker.getEditableColor()){
			mEditColorView.setVisibility(View.VISIBLE);
    		if(mColor != 0){
    			setColor();
    		}
		} else {
			mEditColorView.setVisibility(View.GONE);
		}
	}

	private String buildCategory() {
		String category = "";
		if(mSticker.getNew()){
    		category = category + getString(R.string.page_title_new); 
    	}
    	
    	if(mSticker.getFeatured()){
    		if(!category.equals("")){
    			category = category + " | ";
    		}
    		category = category + getString(R.string.page_title_featured); 
    	}

    	if(mSticker.getPopular()){
    		if(!category.equals("")){
    			category = category + " | ";
    		}
    		category = category + getString(R.string.page_title_popular); 
    	}
		return category;
	}

	public void onLoaderReset(Loader<Cursor> arg0) {
	}
    
    private void onChangeColor(int colorIdResource) {
    	mColorPalette.setVisibility(View.GONE);
    	mEditColorView.setVisibility(View.VISIBLE);
    	mColor = getResources().getColor(colorIdResource);
    	setColor();
    }

	private void setColor() {
    	ColorFilter colorFilter = new LightingColorFilter(Color.BLACK, mColor);
    	mImage.setColorFilter(colorFilter);
	}
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	outState.putInt(Const.ARG_COLOR, mColor);
    }

	private void onSendMail(int mailType) {
		User user = new User(getActivity().getApplicationContext());
		if(user.isEmpty()){
			Intent intent = new Intent(getActivity(), ActivityUserDetail.class);
			intent.putExtra(Const.ARG_MAIL_TYPE, mailType);
			startActivityForResult(intent, REGUEST_CODE_OPEN_USER_INFO);
		} else {
			Utils.sendMail(getActivity(), mSticker, mColor, user, mailType);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REGUEST_CODE_OPEN_USER_INFO){
			if(resultCode == Activity.RESULT_OK){
				onSendMail(data.getIntExtra(Const.ARG_MAIL_TYPE, Const.MAIL_TYPE_QUESTION));
			}
		} else if(requestCode == REQUEST_CODE_NEW_ACTIVITY){
    		String id = String.valueOf(data.getExtras().getInt(Const.ARG_ID));
    		Cursor cursor = getActivity().getContentResolver().query(
    				Uri.withAppendedPath(DbProvider.URI_VISUALIZATION, id),
					null, null, null, null);
    		Visualization visualization = new Visualization(getActivity(), cursor);
    		cursor.close();
    		if(visualization.getBackground() == null){
    			visualization.delete(getActivity());
    		}
    	} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
}
