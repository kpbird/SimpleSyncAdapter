package com.kpbird.simplesyncadapter.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.kpbird.simplesyncadapter.model.ToDoModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ketanparmar on 20/01/14.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private Context ctx;
    private static final String TAG = "DatabaseHelper";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "todo";

    // Table Names
    public static final String TABLE_TODO = "todo";

    // Common column names
    public static final String KEY_ID = "_id";

    // Routes Table - column names
    public static final String KEY_TITLE = "title";
    public static final String KEY_DETAIL = "detail";


    // Table Create Statements
    // route table create statement
    private static final String CREATE_TABLE_ROUTE = "CREATE TABLE "
            + TABLE_TODO + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TITLE + " TEXT,"
            + KEY_DETAIL + " TEXT" + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_ROUTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }



    public ArrayList<ToDoModel> getAllToDos() {
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select * from " + TABLE_TODO, null);
            ArrayList<ToDoModel> arrToDos = new ArrayList<ToDoModel>();
            if (c.moveToFirst()) {
                do {
                    ToDoModel rm = new ToDoModel();
                    rm.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                    rm.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
                    rm.setDesc(c.getString(c.getColumnIndex(KEY_DETAIL)));

                    arrToDos.add(rm);

                } while (c.moveToNext());
            }
            db.close();
            return arrToDos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            db.close();
        }
    }

}
