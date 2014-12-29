package com.kpbird.simplesyncadapter.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;

import com.kpbird.simplesyncadapter.ws.ParseUtil;
import com.parse.ParseUser;

import java.util.UUID;

/**
 * Created by KP on 19/10/14.
 */
public class SyncUtil {
    private String TAG = "SyncUtil";
    private Context ctx;

    public SyncUtil(Context ctx){
        this.ctx =ctx;
    }

    public String getGmailAccount(){
        String acc = null;
        try{
            AccountManager accountManager = (AccountManager) ctx.getSystemService(ctx.ACCOUNT_SERVICE);
            Account accounts[] = accountManager.getAccounts();
            if(accounts ==null){
                acc = "emulator@test.com";
            }
            else{
                for(Account account: accounts){
                    if(account.type.equals("com.google")){
                        acc = account.name;
                    }
                }
            }
            if(acc==null)
                acc = "emulator@test.com";

        }
        catch (Exception e){
            e.printStackTrace();

        }
        return acc;
    }
    public boolean isSyncAccountExists(){
        try {
            AccountManager accountManager = (AccountManager) ctx.getSystemService(ctx.ACCOUNT_SERVICE);
            Account a[]= accountManager.getAccountsByType(SyncConstants.ACCOUNT_TYPE);
            if(a.length >0){
                return true;
            }
            else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void createSyncAccount(String username,String password,String email) throws Exception{

        ParseUtil ps = new ParseUtil();


        ParseUser puser= ps.userSignUp(username, email, password);
        // Create the account type and default account
        Account newAccount = new Account(SyncConstants.ACCOUNT_NAME, SyncConstants.ACCOUNT_TYPE);

        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) ctx.getSystemService(ctx.ACCOUNT_SERVICE);

        Bundle userData = new Bundle();
        userData.putString(SyncConstants.USERDATA_USER_OBJ_ID, puser.getObjectId());
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, "password", userData)) {
            accountManager.setAuthToken(newAccount,SyncConstants.ACCOUNT_TYPE,puser.getObjectId());
            ContentResolver.setIsSyncable(newAccount, SyncConstants.AUTHORITY, 1);
            ContentResolver.setMasterSyncAutomatically(true); // enables AutoSync
            ContentResolver.setSyncAutomatically(newAccount, SyncConstants.AUTHORITY, true);
            ContentResolver.addPeriodicSync(newAccount, SyncConstants.AUTHORITY, new Bundle(), 120);

            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            Log.i(TAG, "Account Created");
        } else {
            Log.i(TAG,"Account Can't Created Some Error Occur");
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }
    }

}
