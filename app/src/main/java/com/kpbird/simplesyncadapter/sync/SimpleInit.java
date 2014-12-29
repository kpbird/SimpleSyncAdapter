package com.kpbird.simplesyncadapter.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.parse.Parse;

/**
 * Created by KP on 27/04/14.
 */
public class SimpleInit extends AsyncTask<Void,Void,Void> {

    String TAG = "AsyncInitSetup";
    private Context ctx;
    public SimpleInit(Context ctx){
        this.ctx = ctx;
    }
    @Override
    protected Void doInBackground(Void... params) {

        try{
            Parse.initialize(ctx, "APPLICATION ID", "CLIENT KEY");

            SyncUtil syncUtils = new SyncUtil(ctx);
            String email = syncUtils.getGmailAccount();
            String pwd = "1234567890";

            if(syncUtils.isSyncAccountExists()){
                Log.i(TAG, "SyncAdapter is already exist");
            }
            else{
                syncUtils.createSyncAccount(email,pwd,email);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
