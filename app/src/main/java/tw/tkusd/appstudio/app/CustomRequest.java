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
import java.sql.CallableStatement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by melon on 2015/7/11.
 */
public class CustomRequest extends Request<JSONObject> {

    public Response.Listener<JSONObject> listener;
    public Map<String, String> params;
    public Map<String, String> headerParams;


    public CustomRequest(int method, String url, Map<String, String> params,
                         Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener) throws AuthFailureError {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.params = params;


    }

    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {

        if(params==null) {
            return  super.getParams();}
        else{
            return params;}
    };

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (headerParams != null) {
            Map<String, String> headerParams = new HashMap<String, String>();
            headerParams.put("Content-Type", "application/json");
            return headerParams;
        }
        return super.getHeaders();
    }


    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            //Allow null
            if (jsonString == null || jsonString.length() == 0) {
                Log.v("Volley","null"+response.statusCode);
                return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
            }
            Log.v("Volley","notnull"+response.statusCode);
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            Log.v("Volley","unsupportedencoding"+response.statusCode);
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            Log.v("Volley","JSONException"+response.statusCode);
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        // TODO Auto-generated method stub
        listener.onResponse(response);
    }
}

