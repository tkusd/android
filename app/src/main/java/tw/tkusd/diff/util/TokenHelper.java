package tw.tkusd.diff.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.io.IOException;
import java.util.UUID;

import tw.tkusd.diff.Constant;
import tw.tkusd.diff.R;
import tw.tkusd.diff.auth.Authenticator;
import tw.tkusd.diff.model.Token;

/**
 * Created by SkyArrow on 2015/9/9.
 */
public class TokenHelper {
    private static TokenHelper instance;

    private final SharedPreferences prefs;
    private final AccountManager accountManager;
    private final String accountType;
    private String currentEmail;

    private TokenHelper(Context context) {
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.accountManager = AccountManager.get(context);
        this.accountType = context.getString(R.string.account_type);
        this.currentEmail = prefs.getString(Constant.PREF_CURRENT_EMAIL, "");
    }

    public static TokenHelper getInstance(Context context) {
        if (instance == null) {
            instance = new TokenHelper(context.getApplicationContext());
        }

        return instance;
    }

    public boolean isLoggedIn(){
        return getCurrentAccount() != null;
    }

    public UUID getToken() {
        Account account = getCurrentAccount();
        if (account == null) return null;

        return getToken(account);
    }

    public UUID getToken(Account account){
        return parseUUID(accountManager.peekAuthToken(account, Authenticator.TYPE_TOKEN));
    }

    public UUID getUserId() {
        Account account = getCurrentAccount();
        if (account == null) return null;

        return getUserId(account);
    }

    public UUID getUserId(Account account){
        return parseUUID(accountManager.getUserData(account, Authenticator.USER_ID));
    }

    public String getCurrentEmail(){
        return currentEmail;
    }

    public Account getCurrentAccount(){
        String currentEmail = getCurrentEmail();

        // Return account directly if current email is not empty
        if (!TextUtils.isEmpty(currentEmail)){
            return getAccount(currentEmail);
        }

        // Return null if no accounts
        Account[] accounts = getAccounts();
        if (accounts.length == 0) return null;

        // Use the first account as current account
        Account account = accounts[0];
        SharedPreferences.Editor editor = prefs.edit();
        this.currentEmail = account.name;

        editor.putString(Constant.PREF_CURRENT_EMAIL, this.currentEmail);
        editor.apply();

        return account;
    }

    public Account getAccount(String email){
        Account[] accounts = getAccounts();

        for (Account account : accounts){
            if (TextUtils.equals(account.name, email)){
                return account;
            }
        }

        return null;
    }

    public Account[] getAccounts(){
        return accountManager.getAccountsByType(accountType);
    }

    public void setAccount(String email, Token token){
        Account account = new Account(email, accountType);
        Bundle bundle = new Bundle();

        bundle.putString(Authenticator.USER_ID, token.getUserId().toString());

        // Add account if not exist
        if (getAccount(email) == null){
            accountManager.addAccountExplicitly(account, null, bundle);
        }

        accountManager.setAuthToken(account, Authenticator.TYPE_TOKEN, token.getId().toString());
    }

    public void removeAccount(Account account){
        if (TextUtils.equals(account.name, currentEmail)){
            SharedPreferences.Editor editor = prefs.edit();
            this.currentEmail = "";
            editor.remove(Constant.PREF_CURRENT_EMAIL);
            editor.apply();
        }

        accountManager.removeAccount(account, new AccountManagerCallback<Boolean>() {
            @Override
            public void run(AccountManagerFuture<Boolean> future) {
                try {
                    boolean result = future.getResult();
                } catch (AuthenticatorException|OperationCanceledException|IOException e){
                    e.printStackTrace();
                }
            }
        }, null);
    }

    public void removeCurrentAccount(){
        Account account = getCurrentAccount();
        if (account == null) return;

        removeAccount(account);
    }

    public static UUID parseUUID(String str) {
        if (TextUtils.isEmpty(str)) return null;

        try {
            return UUID.fromString(str);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
