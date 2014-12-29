package com.kpbird.simplesyncadapter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.SyncStatusObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.kpbird.simplesyncadapter.db.DatabaseHelper;
import com.kpbird.simplesyncadapter.sync.SimpleContentProvider;
import com.kpbird.simplesyncadapter.sync.SimpleInit;
import com.kpbird.simplesyncadapter.sync.SyncConstants;


public class MyActivity extends Activity implements SyncStatusObserver {

    private String TAG = this.getClass().getSimpleName();
    private ListView lstToDo;
    private TextView txtEmpty;
    private SimpleCursorAdapter simpleCursorAdapter;
    private Object mContentProviderHandle;
    private Cursor cursor;
    private SimpleCursorAdapter sca;
    //the desired columns to be bound
    private String[] columns = new String[] {
            DatabaseHelper.KEY_TITLE,
            DatabaseHelper.KEY_DETAIL
    };
    //the XML defined views which the data will be bound to
    private int[] to = new int[] {
            R.id.txtTitle,
            R.id.txtDetail
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        lstToDo = (ListView) findViewById(R.id.lstToDo);
        txtEmpty = (TextView) findViewById(R.id.txtEmpty);
        lstToDo.setEmptyView(txtEmpty);
        SimpleInit init = new SimpleInit(this);
        init.execute();




    }

    @Override
    protected void onResume() {
        super.onResume();
        fillListView();
        mContentProviderHandle= ContentResolver.addStatusChangeListener(ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE,this);
    }
    @Override
    protected void onPause() {
        super.onPause();
         ContentResolver.removeStatusChangeListener(mContentProviderHandle);
    }

    private void fillListView(){
        cursor =  getContentResolver().query(SimpleContentProvider.CONTENT_URI_TODO,null,null,null,null);
        sca = new SimpleCursorAdapter(this,R.layout.row_todo,cursor,columns,to,0);
        lstToDo.setAdapter(sca);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            Account newAccount = new Account(SyncConstants.ACCOUNT_NAME, SyncConstants.ACCOUNT_TYPE);

            Bundle extras = new Bundle();
            // SYNC_EXTRAS_MANUAL enforces manual sync despite automatic sync settings
            extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
            ContentResolver.requestSync(newAccount,SyncConstants.AUTHORITY,extras);



            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStatusChanged(int which) {

         Log.i(TAG, "Which :" + which);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fillListView();
            }
        });
    }
}
