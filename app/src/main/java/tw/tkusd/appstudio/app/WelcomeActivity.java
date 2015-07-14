package tw.tkusd.appstudio.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import tw.tkusd.appstudio.Constant;
import tw.tkusd.appstudio.R;
import tw.tkusd.appstudio.util.RequestHelper;

public class WelcomeActivity extends AppCompatActivity {

    @InjectView(R.id.btn_logout)
    Button btnlogout;

    @InjectView(R.id.result)
    TextView textResult;

    //    JSONObject obj = new JSONObject();
    private RequestHelper mRequestHelper;
    private ProgressDialog pDialog;
    public static final String TAG = WelcomeActivity.class.getSimpleName();
    boolean isTrue;
    String taketoken_id;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.inject(this);
        mRequestHelper = RequestHelper.getInstance(this);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadP();
                post();
                // test();
            }
        });
    }


    public void loadP() {
        int mode = Activity.MODE_PRIVATE;
        SharedPreferences mySharedPreference = getSharedPreferences(LoginActivity.MYPREFS, mode);
        taketoken_id = mySharedPreference.getString("tokenid", "null");
    }
/*
    public void test() throws JSONException {
        showDialog();

        JsonResponseRequest jsonObjectRequest = new JsonResponseRequest(
                Request.Method.DELETE,
                Constant.TOKEN_URL+"/"+taketoken_id,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        textResult.setText(response.toString());
                        Intent newAct = new Intent();
                        newAct.setClass(WelcomeActivity.this, LoginActivity.class);
                        startActivity(newAct);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            try {
                                JSONObject result = new JSONObject(new String(error.networkResponse.data));
                                textResult.setText(result.toString());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        hideDialog();
                    }
                }) {
          //  @Override
          //  protected Map<String, String> getParams() throws AuthFailureError {
           //     HashMap<String, String> params = new HashMap<>();
            //    params.put("tokenid", taketoken_id);
             //   return params;
           // }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
          //      headers.put( "charset", "utf-8");
                return headers;
            }

        };
        mRequestHelper.addToRequestQueue(jsonObjectRequest, TAG);
    }
*/

    //-------------------------------------------------------------------------
    public void post(){
        showDialog();
        JSONObject obj = new JSONObject();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Constant.TOKEN_URL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //textResult.setText(response.toString());
                hideDialog();
                textResult.setText("log success");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textResult.setText(error.toString());

                if (error.networkResponse != null) {
                    try {
                        JSONObject result = new JSONObject(new String(error.networkResponse.data));
                        textResult.setText(result.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                hideDialog();
            }
        });

        mRequestHelper.addToRequestQueue(req, TAG);
    }
    //--------------------------------------------------------------------------------------------------------

    private void showDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
    }

    private void hideDialog() {
        if (pDialog != null){
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_goto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
