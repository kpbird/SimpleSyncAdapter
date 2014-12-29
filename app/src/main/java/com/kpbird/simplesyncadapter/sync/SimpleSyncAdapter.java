package com.kpbird.simplesyncadapter.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.kpbird.simplesyncadapter.db.DatabaseHelper;
import com.kpbird.simplesyncadapter.model.ToDoModel;
import com.kpbird.simplesyncadapter.ws.ParseUtil;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KP on 19/10/14.
 */
public class SimpleSyncAdapter extends AbstractThreadedSyncAdapter {

    private String TAG = "SimpleSyncAdapter";
    private final AccountManager mAccountManager;
    private ParseUtil parseUtil;
    private ParseUser parseUser;

    public SimpleSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mAccountManager = AccountManager.get(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        try{
            Log.i(TAG, "Loading Local data to array");
            // create object of parse utility
            parseUtil = new ParseUtil();
            // fetch current user
            parseUser = ParseUser.getCurrentUser();

            // For simplicity I am implementing only single side sync (From Parse to Local)
            // you need to implement by directional sync in this method
            // delete all local todos
            SyncDelete(provider);
            // pull and add to do in local
            SyncAdd(provider);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void SyncDelete(ContentProviderClient provider){
        try{
            int count =provider.delete(SimpleContentProvider.CONTENT_URI_TODO,null,null);
            Log.i(TAG,"Rows Deleted : "+ count);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    private void SyncAdd(ContentProviderClient provider){
        try{
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(DatabaseHelper.TABLE_TODO);
            List<ParseObject> allTodos=  parseQuery.find();
            for(ParseObject po : allTodos){
                parseUtil.pullToDo(getContext(),po,provider);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
