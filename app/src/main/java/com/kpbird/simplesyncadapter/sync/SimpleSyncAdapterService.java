package com.kpbird.simplesyncadapter.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by KP on 19/10/14.
 */
public class SimpleSyncAdapterService extends Service {

    private String TAG = "SimpleSyncAdapterService";
    @Override
    public IBinder onBind(Intent intent) {
        SimpleSyncAdapter ssa = new SimpleSyncAdapter(getApplicationContext(),true);
        return ssa.getSyncAdapterBinder();
    }
}
