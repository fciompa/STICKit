package cz.ictsystem.stickers.data;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class GenericAccountService extends Service {
    private static final String TAG = "GenericAccountService";

	Authenticator mAuthenticator;

	@Override
    public void onCreate() {
        Log.i(TAG, "Service created");
        mAuthenticator = new Authenticator(this);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service destroyed");
    }

	@Override
	public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
	}
}
