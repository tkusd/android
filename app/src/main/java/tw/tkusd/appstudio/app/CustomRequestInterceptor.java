package tw.tkusd.appstudio.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import retrofit.RequestInterceptor;
import tw.tkusd.appstudio.Constant;

/**
 * Created by melon on 2015/8/4.
 */
public class CustomRequestInterceptor implements RequestInterceptor {
    private SharedPreferences mPref;

    public CustomRequestInterceptor(Context context) {
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void intercept(RequestFacade request) {
        if (mPref!=null) {
            String token = mPref.getString(Constant.PREF_TOKEN, "");
            request.addHeader("Authorization", "Bearer " + token);
        }
    }
}
