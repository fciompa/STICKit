package cz.ictsystem.stickers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import cz.ictsystem.stickers.data.DbProvider;
import cz.ictsystem.stickers.data.Sticker;
import cz.ictsystem.stickers.data.User;

public class VisualizationDetailFragment extends SherlockFragment{

	private final static String TAG = "VisualizationDetailFragment";
	private static final int ACTIVITY_REQUEST_CODE_TAKE_PHOTO = 1;
	private static final int ACTIVITY_REQUEST_CODE_CHOICE_STICKER = 2;
	private static final int ACTIVITY_REQUEST_CODE_CHOICE_GALLERY = 3;
	private final static int ACTIVITY_REGUEST_CODE_OPEN_USER_INFO = 4;
	
	//new
	private View mViewNew;
	private View mNewVisualizationTakePhotoLayout;
	private ImageButton mNewVisualizationTakePhoto;
	private View mNewVisualizationChoiceGalleryLayout;
	private ImageButton mNewVisualizationChoiceGallery;
	private View mNewVisualizationBackgroubdLayout;
	private ImageView mNewVisualizationBackgroubd;
	private ImageButton mNewVisualizationChoiceSticker;
	private ImageView mNewVisualizationSticker;
	private Button mNewVisualizationChoiceEdit;
	//edit image part
	private View mViewEdit;
	private ImageView mImage;
	private EditText mName;
	private ImageButton mNameOK;
	private TextView mUpdateDate;

	//edit color palette
	private View mViewColorPalette;
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
	
	//edit control panel
	private ImageButton mStickerSizeIncrease;
	private ImageButton mStickerSizeDecrease;
	private ImageButton mStickerUp;
	private ImageButton mStickerDown;
	private ImageButton mStickerLeft;
	private ImageButton mStickerRight;
	private ImageButton mStickerColor;
	private ImageButton mStickerFlip;
	private ImageButton mStickerPerspectiveIncrease;
	private ImageButton mStickerPerspectiveDecrease;
	
	private VisualizationBuilder mBuilder;
	private VisualizationStickerToucher mToucher;
	private Uri mPhotoFileUri;
	private boolean mNewVisualization;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.visualization_detail, container, false);
    }	

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	Log.d(TAG, "onActivityCreated");
    	setHasOptionsMenu(true);

    	//Data loading
    	Cursor cursor = getActivity().getContentResolver().query(
				Uri.withAppendedPath(DbProvider.URI_VISUALIZATION, String.valueOf(getArguments().getInt(Const.ARG_ID))),
				null, null, null, null);
    	
    	mBuilder = new VisualizationBuilder(
    			getActivity(), 
    			Utils.getDisplaySize(getActivity()), 
    			new Visualization(getActivity(), cursor));
		cursor.close();


    	if(savedInstanceState != null && savedInstanceState.containsKey(Const.ARG_PHOTO_NEW_VISUALIZATION)){
    		mNewVisualization = savedInstanceState.getBoolean(Const.ARG_PHOTO_NEW_VISUALIZATION);
    	}
    		
    	if(savedInstanceState != null && savedInstanceState.containsKey(Const.ARG_PHOTO_FILE_URI)){
    		mPhotoFileUri = Uri.parse(savedInstanceState.getString(Const.ARG_PHOTO_FILE_URI));
    	}
    	
    	if(savedInstanceState != null && savedInstanceState.containsKey(Const.ARG_NEW_VISUALIZATION_NAME)){
    		mBuilder.getVisualization().setName(savedInstanceState.getString(Const.ARG_NEW_VISUALIZATION_NAME));
    	}

    	mViewNew = getView().findViewById(R.id.visualization_new);
    	//new part
    	mNewVisualizationTakePhotoLayout = getView().findViewById(R.id.visualization_new_take_photo_layout);
    	mNewVisualizationTakePhoto = (ImageButton) getView().findViewById(R.id.visualization_new_take_photo);
    	mNewVisualizationChoiceGalleryLayout = getView().findViewById(R.id.visualization_new_choice_gallery_layout);
    	mNewVisualizationChoiceGallery = (ImageButton) getView().findViewById(R.id.visualization_new_choice_gallery);
    	mNewVisualizationBackgroubdLayout = getView().findViewById(R.id.visualization_new_image_backgroubd_layout);
    	mNewVisualizationBackgroubd = (ImageView) getView().findViewById(R.id.visualization_new_image_backgroubd);
    	mNewVisualizationChoiceSticker = (ImageButton) getView().findViewById(R.id.visualization_new_choice_sticker);
    	mNewVisualizationSticker = (ImageView) getView().findViewById(R.id.visualization_new_image_sticker);
    	mNewVisualizationChoiceEdit = (Button) getView().findViewById(R.id.visualization_new_choice_edit);

    	//edit image part
    	mViewEdit = getView().findViewById(R.id.visualization_edit);
    	mImage = (ImageView) getView().findViewById(R.id.visualization_edit_panel_image_image);
    	mName = (EditText) getView().findViewById(R.id.visualization_edit_panel_image_name);
    	mNameOK = (ImageButton) getView().findViewById(R.id.visualization_edit_panel_image_name_ok);
    	mUpdateDate = (TextView) getView().findViewById(R.id.visualization_edit_panel_image_update_date);
    	
    	//edit color palette
    	mViewColorPalette = (View) getView().findViewById(R.id.visualization_edit_color_palette);
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
    	
    	//edit control 
    	mStickerSizeIncrease = (ImageButton) getView().findViewById(R.id.visualization_edit_panel_control_size_increase);
    	mStickerSizeDecrease = (ImageButton) getView().findViewById(R.id.visualization_edit_panel_control_size_decrease);
    	mStickerUp = (ImageButton) getView().findViewById(R.id.visualization_edit_panel_control_up);
    	mStickerDown = (ImageButton) getView().findViewById(R.id.visualization_edit_panel_control_down);
    	mStickerLeft = (ImageButton) getView().findViewById(R.id.visualization_edit_panel_control_left);
    	mStickerRight = (ImageButton) getView().findViewById(R.id.visualization_edit_panel_control_right);
    	mStickerColor = (ImageButton) getView().findViewById(R.id.visualization_edit_panel_control_color);
    	mStickerFlip = (ImageButton) getView().findViewById(R.id.visualization_edit_panel_control_flip);
    	mStickerPerspectiveIncrease = (ImageButton) getView().findViewById(R.id.visualization_edit_panel_control_perspective_increase);
    	mStickerPerspectiveDecrease = (ImageButton) getView().findViewById(R.id.visualization_edit_panel_control_perspective_decrease);
    	
    	//new part
    	mNewVisualizationTakePhoto.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
	        	takePhoto();
			}
		});
    	
    	mNewVisualizationChoiceGallery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
	        	choiceGallery();
			}
		});
    	
    	mNewVisualizationChoiceSticker.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				choiceSticker();
			}
		});

    	mNewVisualizationChoiceEdit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setVisibility(false);
				mNewVisualization = false;
			}
		});
    	
    	//edit image part
    	if(savedInstanceState != null && savedInstanceState.containsKey(Const.ARG_NEW_VISUALIZATION_NAME)){
    		setEditNameEnable();
    		mName.setText(savedInstanceState.getString(Const.ARG_NEW_VISUALIZATION_NAME));
    	}

    	mNameOK.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mBuilder.getVisualization().setName(mName.getText().toString());
				mName.setEnabled(false);
				mNameOK.setVisibility(View.GONE);
			}
		});

    	//edit color palette
    	if(savedInstanceState != null && savedInstanceState.containsKey(Const.ARG_COLOR_PALATTE_VISIBILITY)){
    		mViewColorPalette.setVisibility(savedInstanceState.getInt(Const.ARG_COLOR_PALATTE_VISIBILITY));
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
    	
    	//edit control part
    	mToucher = new VisualizationStickerToucher(mImage, mBuilder);
    	
    	mStickerSizeIncrease.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mToucher.sizeIncrease(event);
				return true;
			}
		});
    	
    	mStickerSizeDecrease.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mToucher.sizeDecrease(event);
				return true;
			}
		});
    	
    	mStickerUp.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mToucher.moveUp(event);
				return true;
			}
		});

    	mStickerDown.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mToucher.moveDown(event);
				return true;
			}
		});
    	
    	mStickerLeft.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mToucher.moveLeft(event);
				return true;
			}
		});

    	mStickerRight.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mToucher.moveRight(event);
				return true;
			}
		});
    	
    	mStickerColor.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				choiceColor();
			}
		});

    	mStickerFlip.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				flipSticker();
			}
		});

    	mStickerPerspectiveIncrease.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mToucher.perspectiveIncrease(event);
				return true;
			}
		});

    	mStickerPerspectiveDecrease.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mToucher.perspectiveDecrease(event);
				return true;
			}
		});
    	
    	//zooming
		PhotoViewAttacher attacher = new PhotoViewAttacher(mImage);
		attacher.zoomTo((float) 1.2, 0, 0);
		
		setVisibility(isNewVisualization());

    	getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void onChangeColor(int colorIdResource) {
		mBuilder.getVisualization().setColor(getResources().getColor(colorIdResource));
		new VisualizationImageAsyn(mImage).load(mBuilder);
		mViewColorPalette.setVisibility(View.GONE);
    }

	private void saveIntoDatabase() {
		if(mBuilder.getVisualization().getId() != 0){
			mBuilder.getVisualization().setImage(mBuilder.get()).save(getActivity());
        	Utils.mVisualizationCache.remove(mBuilder.getVisualization().getId());
        	Utils.mVisualizationImageCache.remove(mBuilder.getVisualization().getId());
    	}
	}
	
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.visualization_fragment, menu);
        menu.findItem(R.id.menu_take_photo).setEnabled(getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	boolean menuConsumed = false;
        switch (item.getItemId()) {
        case R.id.menu_take_photo:
        	takePhoto();
        	menuConsumed = true;
            break;
        case R.id.menu_choice_photo:
        	choiceGallery();
        	menuConsumed = true;
            break;
        case R.id.menu_choice_sticker:
    		choiceSticker();
        	menuConsumed = true;
            break;
        case R.id.menu_edit_name:
        	setEditNameEnable();
        	break;
        case R.id.menu_delete:
        	onDeleteVisualization();
        	menuConsumed = true;
            break;
        case R.id.menu_share:
        	onShare();
        	menuConsumed = true;
            break;
//        case R.id.menu_web_page:
//        	menuConsumed = true;
//        	Toast.makeText(getActivity(), R.string.menu_not_implemented, Toast.LENGTH_LONG).show();
//            break;
        case R.id.menu_send_question:
        	sendQuestion();
        	menuConsumed = true;
            break;
        case R.id.menu_send_order:
        	sendOrder();
        	menuConsumed = true;
            break;
        default:
        	menuConsumed = super.onOptionsItemSelected(item);
            break;
        }
    	return menuConsumed;
    }

	private void onShare() {
		Utils.shareBitmap(getActivity(), mBuilder.get(), Bitmap.CompressFormat.JPEG);
	}

	private void sendQuestion() {
		onSendMail(Const.MAIL_TYPE_QUESTION);
	}

	private void sendOrder() {
		onSendMail(Const.MAIL_TYPE_ORDER);
	}
	
	private void onSendMail(int mailType) {
		User user = new User(getActivity().getApplicationContext());
		if(user.isEmpty()){
			Intent intent = new Intent(getActivity(), ActivityUserDetail.class);
			intent.putExtra(Const.ARG_MAIL_TYPE, mailType);
			startActivityForResult(intent, ACTIVITY_REGUEST_CODE_OPEN_USER_INFO);
		} else {
			Utils.sendMail(getActivity(), mBuilder.getSticker(), mBuilder.getVisualization().getColor(), user, mailType);
		}
	}
	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	switch (requestCode){
    		case ACTIVITY_REQUEST_CODE_TAKE_PHOTO:
    			if(resultCode == Activity.RESULT_OK){
    				onTakePhoto(intent);
    			}
    			break;
    		case ACTIVITY_REQUEST_CODE_CHOICE_STICKER:
    			if(resultCode == Activity.RESULT_OK){
    				onChangeSticker(intent);	
    			}
    			break;
    		case ACTIVITY_REQUEST_CODE_CHOICE_GALLERY:
    			if(resultCode == Activity.RESULT_OK){
    				onChoiceGallery(intent);	
    			}
    			break;
    		
    		case ACTIVITY_REGUEST_CODE_OPEN_USER_INFO:
				if(resultCode == Activity.RESULT_OK){
					onSendMail(intent.getIntExtra(Const.ARG_MAIL_TYPE, Const.MAIL_TYPE_QUESTION));
				}
    			
    		default:
    			super.onActivityResult(requestCode, resultCode, intent);
    	}
    }

	protected boolean isNewVisualization() {
		mNewVisualization =
				mNewVisualization ||
				mBuilder.getSticker().getId() == 0 || 
				mBuilder.getVisualization().getBackground() == null; 
		return mNewVisualization;
	}

	private void setVisibility(boolean newVisualization) {
		mViewEdit.setVisibility(newVisualization ? View.GONE : View.VISIBLE);
		mViewNew.setVisibility(newVisualization ? View.VISIBLE : View.GONE);
		
		if(newVisualization){
			setNewVisualizationBackground();
			setNewVisualizationSticker();
		} else {
			setEditVisualizationImage();
		}
	}

	public void onLoaderReset(Loader<Cursor> arg0) {
	}
	
    private void takePhoto() {
		if(Utils.isIntentAvailable(MediaStore.ACTION_IMAGE_CAPTURE, getActivity().getApplicationContext())){
		    String mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
		        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
		        mPhotoFileUri = Uri.fromFile(
		        		new java.io.File(mediaStorageDir + java.io.File.separator + "IMG_" + timeStamp + Const.FILE_SUFFIX_JPG));

		        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoFileUri);
		        startActivityForResult(cameraIntent, ACTIVITY_REQUEST_CODE_TAKE_PHOTO);		    
		} else {
			Toast.makeText(getActivity(), R.string.menu_not_app_capture_image, Toast.LENGTH_LONG).show();
		}
	}
    
	private void onTakePhoto(Intent intent) {
		onChangeBackground(mPhotoFileUri.getPath());
	}
	private void choiceSticker() {
		Intent intent = new Intent(getActivity().getApplicationContext(), ActivityStickers.class);
		intent.putExtra(Const.ARG_CHOICE, true);
		startActivityForResult(intent, ACTIVITY_REQUEST_CODE_CHOICE_STICKER);
	}
    
	private void onChangeSticker(Intent intent){
		int stickerId = intent.getExtras().getInt(Const.ARG_ID);
		Sticker sticker = new Sticker (getActivity(), stickerId);
		mBuilder.setSticker(sticker);
		mStickerColor.setEnabled(sticker.getEditableColor());
		if(!isNewVisualization()){
			new VisualizationImageAsyn(mImage).load(mBuilder);
		} else {
			setNewVisualizationSticker();
		}
	}
	
	private void setEditVisualizationImage(){
		mName.setText(String.valueOf(mBuilder.getVisualization().getName()));
		mUpdateDate.setText(DateFormat.getDateInstance().format(mBuilder.getVisualization().getUpdateDate()));
		mStickerColor.setEnabled(mBuilder.getSticker().getEditableColor());
		new VisualizationImageAsyn(mImage).load(mBuilder);
	}
	
	private void setNewVisualizationBackground(){
		boolean background = (mBuilder.getVisualization().getBackground() != null);
		boolean sticker = (mBuilder.getVisualization().getStickerId() != 0);
		
		mNewVisualizationTakePhotoLayout.setVisibility(background ? View.GONE : View.VISIBLE);
		mNewVisualizationChoiceGalleryLayout.setVisibility(background ? View.GONE : View.VISIBLE);
		mNewVisualizationBackgroubdLayout.setVisibility(background ? View.VISIBLE : View.GONE);
		
		if(background){
			mNewVisualizationBackgroubd.setImageBitmap(
					Utils.getBitmapFromBlob(mBuilder.getVisualization().getBackground(), 
							Utils.getDisplaySize(getActivity()).x, 
							Utils.getDisplaySize(getActivity()).y));
		}
		
		mNewVisualizationChoiceEdit.setEnabled(background && sticker);		
	}
	
	private void setNewVisualizationSticker(){
		boolean background = (mBuilder.getVisualization().getBackground() != null);
		boolean sticker = (mBuilder.getVisualization().getStickerId() != 0);
		
		mNewVisualizationChoiceSticker.setVisibility(sticker ? View.GONE : View.VISIBLE);
		mNewVisualizationSticker.setVisibility(sticker ? View.VISIBLE : View.GONE);
		
		if(sticker){
			mNewVisualizationSticker.setImageBitmap(
					Utils.getBitmapFromBlob(mBuilder.getSticker().getImage(),
							Utils.getDisplaySize(getActivity()).x,
							Utils.getDisplaySize(getActivity()).y));
		}
		
		mNewVisualizationChoiceEdit.setEnabled(background && sticker);		
	}
	private void choiceGallery(){
		Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), ACTIVITY_REQUEST_CODE_CHOICE_GALLERY);		
	}
	
	private void onChoiceGallery(Intent intent){
		String selectedImagePath = "";
		Uri selectedImageUri = intent.getData();
		if(selectedImageUri.getScheme().equals("content")){
		    String[] projection = { MediaStore.Images.Media.DATA };
		    Cursor cursor = getActivity().getContentResolver().query(selectedImageUri, projection, null, null, null);
		    cursor.moveToFirst();
			selectedImagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
		} else if (selectedImageUri.getScheme().equals("file")){
			selectedImagePath = selectedImageUri.getPath();
		} else {
			Toast.makeText(getActivity(), "Unknown background source type", Toast.LENGTH_LONG).show();
		}
		onChangeBackground(selectedImagePath);		
	}

	public void onChangeBackground(String selectedImagePath) {
		Log.d(TAG, "onChangeBackground");
		byte[] background = 
				Utils.getBlobJPEG(Utils.getBitmapFromFile(selectedImagePath,
				Utils.getDisplaySize(getActivity()).x,
				Utils.getDisplaySize(getActivity()).y));
		mBuilder.getVisualization().setBackground(background);
		if(!isNewVisualization()){
			new VisualizationImageAsyn(mImage).load(mBuilder);
		} else {
			setNewVisualizationBackground();
		}
	}
	
	private void choiceColor() {
		if(mViewColorPalette.getVisibility() == View.GONE){
			mViewColorPalette.setVisibility(View.VISIBLE);
		} else {
			mViewColorPalette.setVisibility(View.GONE);
		}
	}

	private void flipSticker() {
		mBuilder.flipSticker();
		new VisualizationImageAsyn(mImage).load(mBuilder);
	}

	private void setEditNameEnable(){
		mName.setEnabled(true);
		mNameOK.setVisibility(View.VISIBLE);
	}

	private boolean getEditNameEnable(){
		return mNameOK.getVisibility() == View.VISIBLE;
	}

	private void onDeleteVisualization(){
		Utils.mVisualizationCache.remove(mBuilder.getVisualization().getId());
    	Utils.mVisualizationImageCache.remove(mBuilder.getVisualization().getId());
    	mBuilder.getVisualization().delete(getActivity());
		getActivity().finish();
	}

	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d(TAG, "onSaveInstanceStatte");
		if (mPhotoFileUri != null){
			outState.putString(Const.ARG_PHOTO_FILE_URI, mPhotoFileUri.getPath());
		}
		
		outState.putBoolean(Const.ARG_PHOTO_NEW_VISUALIZATION, mNewVisualization);
		outState.putInt(Const.ARG_COLOR_PALATTE_VISIBILITY, mViewColorPalette.getVisibility());
		if(getEditNameEnable()){
			outState.putString(Const.ARG_NEW_VISUALIZATION_NAME, mName.getText().toString());
		}
		saveIntoDatabase();
	}

	@Override
	public void onPause() {
		super.onPause();
		saveIntoDatabase();
	}
}
