package cz.ictsystem.stickers.data;

import android.content.Context;
import android.content.SharedPreferences;

public class User {

	private static final String SHARED_PREFERENCES = "cz.ictsystem.sticker.user";
	private static final String NAME = "mName";
	private static final String SURNAME = "mSurname";
	private static final String FIRM = "mFirm";
	private static final String ICO = "mIco";
	private static final String DIC = "mDic";
	private static final String ADDRESS = "mAddress";
	private static final String ZIP_CODE = "mZipCode";
	private static final String CITY = "mCity";
	private static final String PHONE = "mPhone";
	private static final String EMAIL = "mEmail";

	private static final String DEF_VALUE = "";
	
	private Context mContext;
	
	private String mName;
	private String mSurname;
	private String mFirm;
	private String mIco;
	private String mDic;
	private String mAddress;
	private String mZipCode;
	private String mCity;
	private String mPhone;
	private String mEmail;
	
	public User(Context context){
		mContext = context;
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		mName = sharedPreferences.getString(NAME, DEF_VALUE);
		mSurname= sharedPreferences.getString(SURNAME, DEF_VALUE);
		mFirm= sharedPreferences.getString(FIRM, DEF_VALUE);
		mIco = sharedPreferences.getString(ICO, DEF_VALUE);
		mDic = sharedPreferences.getString(DIC, DEF_VALUE);
		mAddress = sharedPreferences.getString(ADDRESS, DEF_VALUE);
		mZipCode = sharedPreferences.getString(ZIP_CODE, DEF_VALUE);
		mCity = sharedPreferences.getString(CITY, DEF_VALUE);
		mPhone = sharedPreferences.getString(PHONE, DEF_VALUE);
		mEmail = sharedPreferences.getString(EMAIL, DEF_VALUE);
		
	}
	
	public void save(){
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(NAME, mName);
		editor.putString(SURNAME, mSurname);
		editor.putString(FIRM, mFirm);
		editor.putString(ICO, mIco);
		editor.putString(DIC, mDic);
		editor.putString(ADDRESS, mAddress);
		editor.putString(ZIP_CODE, mZipCode);
		editor.putString(CITY, mCity);
		editor.putString(PHONE, mPhone);
		editor.putString(EMAIL, mEmail);
		
		editor.commit();		
	}
	
	public boolean isEmpty(){
		return mEmail.equals(DEF_VALUE);  
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public String getSurname() {
		return mSurname;
	}

	public void setSurname(String surname) {
		this.mSurname = surname;
	}

	public String getFirm() {
		return mFirm;
	}

	public void setFirm(String firm) {
		this.mFirm = firm;
	}

	public String getIco() {
		return mIco;
	}

	public void setIco(String ico) {
		this.mIco = ico;
	}

	public String getDic() {
		return mDic;
	}

	public void setDic(String dic) {
		this.mDic = dic;
	}

	public String getAddress() {
		return mAddress;
	}

	public void setAddress(String address) {
		this.mAddress = address;
	}

	public String getZipCode() {
		return mZipCode;
	}

	public void setZipCode(String zipCode) {
		this.mZipCode = zipCode;
	}

	public String getCity() {
		return mCity;
	}

	public void setCity(String city) {
		this.mCity = city;
	}

	public String getPhone() {
		return mPhone;
	}

	public void setPhone(String phone) {
		this.mPhone = phone;
	}

	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String email) {
		this.mEmail = email;
	}
}
