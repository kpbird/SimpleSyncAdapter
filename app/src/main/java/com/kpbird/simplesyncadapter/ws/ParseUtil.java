package com.kpbird.simplesyncadapter.ws;

import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.kpbird.simplesyncadapter.db.DatabaseHelper;
import com.kpbird.simplesyncadapter.sync.SimpleContentProvider;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by KP on 19/10/14.
 */
public class ParseUtil {
    private final String TAG = "ParseUtil";

    public ParseUser userSignUp(String name, String email, String pass) throws Exception {

        Log.i(TAG, "userSignUp");
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo("email", email);
        List<ParseUser> lstPU = parseQuery.find();
        if (lstPU.size() > 0) {
            Log.i(TAG, "Parse User Already Exist");
            return lstPU.get(0);
        } else {
            Log.i(TAG, "Create new Parse User");
            ParseUser pu = new ParseUser();
            pu.setEmail(email);
            pu.setUsername(name);
            pu.setPassword(pass);

            pu.signUp();
            createToDoClass();
            return pu;
        }

    }
    public void createToDoClass(){
        try {
            ParseObject todoClass = new ParseObject(DatabaseHelper.TABLE_TODO);
            todoClass.put(DatabaseHelper.KEY_TITLE, "Task 1");
            todoClass.put(DatabaseHelper.KEY_DETAIL, "Detail 1");
            todoClass.save();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public ParseUser userSignIn(String user, String pass) throws Exception {

        Log.d(TAG, "userSignIn");

        ParseUser pu = ParseUser.logIn(user, pass);
        Log.d(TAG, "Session Token " + pu.getSessionToken());

        return pu;

    }

    public void pullToDo(Context ctx, ParseObject po, ContentProviderClient provider) {
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.KEY_TITLE, po.getString(DatabaseHelper.KEY_TITLE));
            values.put(DatabaseHelper.KEY_DETAIL, po.getString(DatabaseHelper.KEY_DETAIL));
            Uri uri = provider.insert(SimpleContentProvider.CONTENT_URI_TODO, values);
            Log.i(TAG,"Insert URI :" + uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
