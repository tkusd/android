package tw.tkusd.appstudio.app;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wenlin on 2015/7/17.
 */
public class CustomReq extends Request<JSONObject> {

    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;
    private String postString = null;
    public String jsonBody;

    public CustomReq(String url, Map<String, String> params,
                               Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    public CustomReq(int method, String url, Map<String, String> params,
                               Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Accept", "application/json");
        return headers;
    }

    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    };
    @Override
    public byte[] getBody() throws AuthFailureError {
        if ((getMethod() == Method.DELETE) && (jsonBody != null)) {
            return jsonBody.getBytes();
        } else {
            return super.getBody();
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        // TODO Auto-generated method stub
        listener.onResponse(response);
    }



    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            //Allow null
            if (jsonString == null || jsonString.length() == 0) {
                Log.v("Volley", "null " + response.statusCode);
                return Response.success(new JSONObject(), HttpHeaderParser.parseCacheHeaders(response));
            }
            Log.v("Volley", "notnull " + response.statusCode);
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            Log.v("Volley", "unsupportedencoiding " + response.statusCode);
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            Log.v("Volley", "JSONException " + response.statusCode);
            return Response.error(new ParseError(je));
        }
    }
}