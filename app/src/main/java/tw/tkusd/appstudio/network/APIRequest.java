package tw.tkusd.appstudio.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

import tw.tkusd.appstudio.Constant;

public abstract class APIRequest<T> extends Request<T> {
    protected static final String PROTOCOL_CHARSET = "UTF-8";

    private Context mContext;
    private Response.Listener<T> mListener;
    private byte[] mBody;
    private SharedPreferences mPref;

    public APIRequest(Context context, int method, String url, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mContext = context;
        mListener = listener;

        if (context != null) {
            mPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        }
    }

    public APIRequest(Context context, int method, String url, byte[] body, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(context, method, url, listener, errorListener);
        mBody = body;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public Response.Listener<T> getListener() {
        return mListener;
    }

    public void setListener(Response.Listener<T> listener) {
        mListener = listener;
    }

    public void setBody(byte[] body) {
        mBody = body;
    }

    @Override
    protected abstract Response<T> parseNetworkResponse(NetworkResponse response);

    @Override
    protected void deliverResponse(T response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();

        headers.put("Content-Type", "application/json");

        if (mPref != null) {
            String token = mPref.getString(Constant.PREF_TOKEN, "");
            headers.put("Authorization", "Bearer " + token);
        }

        return headers;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return mBody;
    }
}
