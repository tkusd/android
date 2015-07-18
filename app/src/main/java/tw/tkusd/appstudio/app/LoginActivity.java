package tw.tkusd.appstudio.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import tw.tkusd.appstudio.Constant;
import tw.tkusd.appstudio.R;
import tw.tkusd.appstudio.util.RequestHelper;

/**
 * Created by melon on 2015/6/7.
 */
public class LoginActivity extends AppCompatActivity{

    @InjectView(R.id.btn_send_request)
    Button btnSendRequest;

    @InjectView(R.id.txtsignup)
    TextView txtsignup;

    @InjectView(R.id.login_check)
    CheckBox login_check;

    @InjectView(R.id.email)
    EditText inputEmail;

    @InjectView(R.id.password)
    EditText inputPassword;

    @InjectView(R.id.result)
    TextView textResult;

    private RequestHelper mRequestHelper;
    private ProgressDialog pDialog;
    public static final String TAG = LoginActivity.class.getSimpleName();
    public static final String MYPREFS="MyCustomSharedPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.inject(this);
        mRequestHelper = RequestHelper.getInstance(this);

        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    post();
                } catch (AuthFailureError authFailureError) {
                    authFailureError.printStackTrace();
                }
                if (login_check.isChecked()) {
                    int mode = Activity.MODE_PRIVATE;
                    SharedPreferences mySharedPreference = getSharedPreferences(MYPREFS, mode);
                    SharedPreferences.Editor editor = mySharedPreference.edit();
                    editor.putBoolean("isTrue", true);
                    editor.putString("email",inputEmail.getText().toString() );
                    editor.commit();
                }

            }
        });
        txtsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newAct = new Intent();
                newAct.setClass(LoginActivity.this, MainActivity.class);
                startActivity(newAct);
            }
        });
    }

    @Override
    protected void onDestroy() {
        mRequestHelper.cancelAllRequests(TAG);
        super.onDestroy();
    }

    public void post() throws AuthFailureError {
        showDialog();
        CustomRequest req = new CustomRequest(Request.Method.POST, Constant.TOKEN_URL,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideDialog();
                textResult.setText("log success");
                if(response!=null) {
                    try {
//                            token_id = settingsActivity.getString("token_id", "");
//                            int mode = Activity.MODE_PRIVATE;
//                            SharedPreferences mySharedPreference = getSharedPreferences(MYPREFS, mode);
//                            SharedPreferences.Editor editor=settingsActivity.edit();
//                            editor.putBoolean("isTrue", true);
//                            editor.putString("token_id",response.getString("id"));
//                            editor.commit();
                        int mode = Activity.MODE_PRIVATE;
                        SharedPreferences mySharedPreference = getSharedPreferences(MYPREFS, mode);
                        SharedPreferences.Editor editor = mySharedPreference.edit();
                        editor.putBoolean("isTrue", true);
                        String gettokenid = response.getString("id");
                        editor.putString("tokenid", gettokenid);
                        editor.commit();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent newAct = new Intent();
                    newAct.setClass(LoginActivity.this, WelcomeActivity.class);
                    startActivity(newAct);
                }
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
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("email", inputEmail.getText().toString());
                params.put("password", inputPassword.getText().toString());
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                //headers.put("Content-Type", "application/json");
                headers.put( "charset", "charset=utf-8");
                return headers;
            }

        };

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


}

