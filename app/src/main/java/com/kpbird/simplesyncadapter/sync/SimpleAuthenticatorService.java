package com.kpbird.simplesyncadapter.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by KP on 19/10/14.
 */
public class SimpleAuthenticatorService extends Service {

    private String TAG = "SimpleAuthenticatorService";
    @Override
    public IBinder onBind(Intent intent) {
        SimpleAuthenticator sa = new SimpleAuthenticator(this);
        return sa.getIBinder();
    }
}
