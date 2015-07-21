package tw.tkusd.appstudio.network;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class APIArrayRequest extends APIRequest<JSONArray> {
    public APIArrayRequest(Context context, int method, String url, JSONArray body, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(context, method, url, body, listener, errorListener);
    }

    public APIArrayRequest(Context context, int method, String url, JSONObject body, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(context, method, url, body, listener, errorListener);
    }

    public APIArrayRequest(Context context, int method, String url, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(context, method, url, listener, errorListener);
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            if (response.statusCode == 204 && response.data == null) {
                return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
            }

            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

            return Response.success(new JSONArray(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }
}
