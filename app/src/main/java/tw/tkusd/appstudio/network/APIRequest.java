package tw.tkusd.appstudio.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public abstract class APIRequest<T> extends Request<T> {
    protected static final String PROTOCOL_CHARSET = "UTF-8";

    private Context mContext;
    private Response.Listener<T> mListener;
    private byte[] mBody;

    public APIRequest(Context context, int method, String url, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mContext = context;
        mListener = listener;
    }

    public APIRequest(Context context, int method, String url, JSONObject body, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(context, method, url, listener, errorListener);
        mBody = body.toString().getBytes();
    }

    public APIRequest(Context context, int method, String url, JSONArray body, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(context, method, url, listener, errorListener);
        mBody = body.toString().getBytes();
    }

    public Context getContext() {
        return mContext;
    }

    public Response.Listener<T> getListener() {
        return mListener;
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

        return headers;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return mBody;
    }
}
