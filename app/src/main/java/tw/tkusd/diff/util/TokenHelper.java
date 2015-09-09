package tw.tkusd.diff.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.UUID;

import tw.tkusd.diff.Constant;
import tw.tkusd.diff.model.Token;

/**
 * Created by SkyArrow on 2015/9/9.
 */
public class TokenHelper {
    private static TokenHelper instance;

    private final Context context;
    private final SharedPreferences prefs;
    private UUID token;
    private UUID userId;

    private TokenHelper(Context context) {
        this.context = context;
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.token = parseUUID(prefs.getString(Constant.PREF_TOKEN, ""));
        this.userId = parseUUID(prefs.getString(Constant.PREF_USER_ID, ""));
    }

    public static TokenHelper getInstance(Context context) {
        if (instance == null) {
            instance = new TokenHelper(context.getApplicationContext());
        }

        return instance;
    }

    public UUID getToken(){
        return token;
    }

    public UUID getUserId(){
        return userId;
    }

    public void setToken(Token token){
        SharedPreferences.Editor editor = prefs.edit();
        this.token = token.getId();
        this.userId = token.getUserId();

        editor.putString(Constant.PREF_TOKEN, this.token.toString());
        editor.putString(Constant.PREF_USER_ID, this.userId.toString());

        editor.apply();
    }

    public static UUID parseUUID(String str){
        if (str == null || str.isEmpty()) return null;

        try {
            return UUID.fromString(str);
        } catch (IllegalArgumentException e){
            return null;
        }
    }
}
