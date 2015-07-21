package tw.tkusd.appstudio.network;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class APIObjectRequest extends APIRequest<JSONObject> {
    public APIObjectRequest(Context context, int method, String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(context, method, url, listener, errorListener);
    }

    public APIObjectRequest(Context context, int method, String url, JSONObject body, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(context, method, url, listener, errorListener);

        try {
            setBody(body.toString().getBytes(PROTOCOL_CHARSET));
        } catch (UnsupportedEncodingException e) {
            // Ignore the error
        }
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            if (response.statusCode == 204 && response.data == null) {
                return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
            }

            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }
}
