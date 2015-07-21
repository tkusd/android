package tw.tkusd.appstudio.network;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;

public class APIStringRequest extends APIRequest<String> {
    public APIStringRequest(Context context, int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(context, method, url, listener, errorListener);
    }

    public APIStringRequest(Context context, int method, String url, String body, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(context, method, url, listener, errorListener);

        try {
            setBody(body.getBytes(PROTOCOL_CHARSET));
        } catch (UnsupportedEncodingException e) {
            // Ignore the error
        }
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        if (response.statusCode == 204 && response.data == null) {
            return Response.success("", HttpHeaderParser.parseCacheHeaders(response));
        }

        String parsed;

        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }

        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }
}
