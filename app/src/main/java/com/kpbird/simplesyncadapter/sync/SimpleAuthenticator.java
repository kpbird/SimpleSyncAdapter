package com.kpbird.simplesyncadapter.sync;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.kpbird.simplesyncadapter.ws.ParseUtil;
import com.parse.ParseUser;

import static android.accounts.AccountManager.KEY_BOOLEAN_RESULT;

/**
 * Created by KP on 19/10/14.
 */
public class SimpleAuthenticator extends AbstractAccountAuthenticator {

    private String TAG = "SimpleAuthenticator";
    private final Context mContext;
    public SimpleAuthenticator(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        Log.i(TAG, "editProperties");
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        Log.i(TAG, "addAccount");
        return null;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        Log.i(TAG, "confirmCredentials");
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        Log.i(TAG, "getAuthToken");
        // If the caller requested an authToken type we don't support, then
        // return an error
        if (!authTokenType.equals(SyncConstants.AUTHTOKEN_TYPE_READ_ONLY) && !authTokenType.equals(SyncConstants.AUTHTOKEN_TYPE_FULL_ACCESS)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType");
            return result;
        }

        // Extract the username and password from the Account Manager, and ask
        // the server for an appropriate AuthToken.
        final AccountManager am = AccountManager.get(mContext);

        String authToken = am.peekAuthToken(account, authTokenType);
        String userId = null; //User identifier, needed for creating ACL on our server-side

        Log.d(TAG , "> peekAuthToken returned - " + authToken);

        // Lets give another try to authenticate the user
        if (TextUtils.isEmpty(authToken)) {
            final String password = am.getPassword(account);
            if (password != null) {
                try {
                    Log.d( TAG , "> re-authenticating with the existing password");
                    ParseUtil parseServer = new ParseUtil();
                    ParseUser user = parseServer.userSignIn(account.name, password);
                    if (user != null) {
                        authToken = user.getSessionToken();
                        userId = user.getObjectId();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // If we get an authToken - we return it
        if (!TextUtils.isEmpty(authToken)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            return result;
        }

        return null;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        Log.i(TAG, "getAuthTokenLabel");
        Log.i(TAG,"getAuthTokenLabel");
        if (SyncConstants.AUTHTOKEN_TYPE_FULL_ACCESS.equals(authTokenType))
            return SyncConstants.AUTHTOKEN_TYPE_FULL_ACCESS_LABEL;
        else if (SyncConstants.AUTHTOKEN_TYPE_READ_ONLY.equals(authTokenType))
            return SyncConstants.AUTHTOKEN_TYPE_READ_ONLY_LABEL;
        else
            return authTokenType + " (Label)";
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        Log.i(TAG, "updateCredentials");
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        Log.i(TAG, "hasFeatures");
        final Bundle result = new Bundle();
        result.putBoolean(KEY_BOOLEAN_RESULT, false);
        return result;
    }
}
