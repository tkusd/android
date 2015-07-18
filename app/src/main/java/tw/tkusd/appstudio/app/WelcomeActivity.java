package tw.tkusd.appstudio.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    @InjectView(R.id.txtResult)
    TextView txtResult;


    private RequestHelper mRequestHelper;
    private ProgressDialog pDialog;
    public static final String TAG = WelcomeActivity.class.getSimpleName();
    String taketoken_id;
    String ID_URL=Constant.TOKEN_URL+"/"+taketoken_id;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.inject(this);
        mRequestHelper = RequestHelper.getInstance(this);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getHeaders();
                    loadPreference();
                    delete();
                    removePreference();
                    if (taketoken_id == "") {
                        txtResult.setText("success");

                        Intent newAct = new Intent();
                        newAct.setClass(WelcomeActivity.this, LoginActivity.class);
                        startActivity(newAct);
                    } else {
                        txtResult.setText("delete failed");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (AuthFailureError authFailureError) {
                    authFailureError.printStackTrace();
                }
            }
        });
    }

    public void loadPreference() {
        int mode = Activity.MODE_PRIVATE;
        SharedPreferences mySharedPreference = getSharedPreferences(LoginActivity.MYPREFS, mode);
        taketoken_id = mySharedPreference.getString("tokenid", "null");
    }

    public void removePreference() {
        SharedPreferences settings =getSharedPreferences(LoginActivity.MYPREFS,MODE_PRIVATE);
        settings.edit().clear().commit();
        taketoken_id=settings.getString("tokenid","");

    }
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        return headers;}




    private void delete() throws JSONException, AuthFailureError {
        showDialog();
        JSONObject obj = new JSONObject();

        CustomRequest req = new CustomRequest(Request.Method.DELETE, ID_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideDialog();
                Toast.makeText(WelcomeActivity.this, "登出成功", Toast.LENGTH_SHORT);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    try {
                        JSONObject result = new JSONObject(new String(error.networkResponse.data));
                        Toast.makeText(WelcomeActivity.this,result.toString(),Toast.LENGTH_SHORT);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                hideDialog();
            }
        });


        mRequestHelper.addToRequestQueue(req, TAG);
    }

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
