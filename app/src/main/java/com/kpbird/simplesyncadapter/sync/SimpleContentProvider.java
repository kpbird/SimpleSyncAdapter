package com.kpbird.simplesyncadapter.sync;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.kpbird.simplesyncadapter.db.DatabaseHelper;

import java.util.HashMap;

/**
 * Created by KP on 19/10/14.
 */
public class SimpleContentProvider extends ContentProvider {

    private String TAG = "SimpleContentProvider";
    public static final String PROVIDER_NAME = "com.kpbird.simplesyncadapter";
    public static final Uri CONTENT_URI_TODO = Uri.parse("content://" + PROVIDER_NAME + "/todo");
    private static HashMap<String, String> TODO_PROJECTION_MAP;
    private SQLiteDatabase db;

    static final int TODO = 1;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "todo", TODO);
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return (db == null) ? false : true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(DatabaseHelper.TABLE_TODO);
        qb.setProjectionMap(TODO_PROJECTION_MAP);
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        return "vnd.android.cursor.dir/vnd.simplesyncadapter.todo";
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID=0;
        rowID=  db.insert(DatabaseHelper.TABLE_TODO,"",values);
        if (rowID > 0)
        {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI_TODO, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        else{
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        count = db.delete(DatabaseHelper.TABLE_TODO, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
