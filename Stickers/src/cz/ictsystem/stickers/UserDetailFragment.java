package cz.ictsystem.stickers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import cz.ictsystem.stickers.data.User;



public class UserDetailFragment  extends SherlockFragment{

	EditText mJmeno;
	EditText mPrijmeni;
	EditText mFirma;
	EditText mAdresa;
	EditText mPsc;
	EditText mMesto;
	EditText mTelefon;
	EditText mEmail;

	User mUserData;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.user_detail, container, false);
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
    	setHasOptionsMenu(true);
    	
    	mJmeno = (EditText) getView().findViewById(R.id.user_first_name);
		mPrijmeni = (EditText) getView().findViewById(R.id.user_surname);
		mFirma = (EditText) getView().findViewById(R.id.user_firm);
		mAdresa = (EditText) getView().findViewById(R.id.user_address);
		mPsc = (EditText) getView().findViewById(R.id.user_zip_code);
		mMesto = (EditText) getView().findViewById(R.id.mesto);
		mTelefon = (EditText) getView().findViewById(R.id.telefon);
		mEmail = (EditText) getView().findViewById(R.id.user_email);
		
		mUserData = new User(getActivity());

		mJmeno.setText(mUserData.getName());
		mPrijmeni.setText(mUserData.getSurname());
		mFirma.setText(mUserData.getFirm());
		mAdresa.setText(mUserData.getAddress());
		mPsc.setText(mUserData.getZipCode());
		mMesto.setText(mUserData.getCity());
		mTelefon.setText(mUserData.getPhone());
		mEmail.setText(mUserData.getEmail());
}
	
	@Override
	public void onResume() {
		super.onResume();
    	getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	@Override
	public void onPause() {
		mUserData.setName(mJmeno.getText().toString());
		mUserData.setSurname(mPrijmeni.getText().toString());
		mUserData.setFirm(mFirma.getText().toString());
		mUserData.setAddress(mAdresa.getText().toString());
		mUserData.setZipCode(mPsc.getText().toString());
		mUserData.setCity(mMesto.getText().toString());
		mUserData.setPhone(mTelefon.getText().toString());
		mUserData.setEmail(mEmail.getText().toString());
		mUserData.save();
		super.onPause();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	inflater.inflate(R.menu.user_fragment, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	boolean menuConsumed = false;
        switch (item.getItemId()) {
	        case android.R.id.home:
	            getActivity().finish();
	            menuConsumed = true;
	            break;
	        case R.id.menu_ok:
	        	onOK();
	            menuConsumed = true;
	            break;
	        default:
	        	menuConsumed = super.onOptionsItemSelected(item);
	            break;
        }
        return menuConsumed;
	}
	
	private void onOK() {
		Intent intent = new Intent();
		Intent activityIntent = getActivity().getIntent();
		if(activityIntent.hasExtra(Const.ARG_MAIL_TYPE)){
			intent.putExtra(Const.ARG_MAIL_TYPE, activityIntent.getExtras().getInt(Const.ARG_MAIL_TYPE));
			intent.putExtra(Const.ARG_ID, activityIntent.getExtras().getInt(Const.ARG_ID));
		}
		if(mUserData.isEmpty()){
			Toast.makeText(getActivity(), R.string.user_toast_empty_email, Toast.LENGTH_LONG).show();
		}
		getActivity().setResult(Activity.RESULT_OK, intent);
		getActivity().finish();
	}
}