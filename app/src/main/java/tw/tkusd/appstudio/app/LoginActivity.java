package tw.tkusd.appstudio.app;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public static final String TAG = LoginActivity.class.getSimpleName();
    public static String test;

    @InjectView(R.id.btn_send_request)
    Button btnSendRequest;
    @InjectView(R.id.btn_signup)
    Button btnsignup;

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
    SharedPreferences settingsActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.inject(this);
        settingsActivity=getSharedPreferences("MyCustomSharedPreferences", 0);
        String mystring = settingsActivity.getString("mystring", "");
        inputEmail.setText(mystring);
        mRequestHelper = RequestHelper.getInstance(this);

        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
                if (login_check.isChecked()) {
                    SharedPreferences.Editor editor = settingsActivity.edit();
                    editor.putString("mystring", inputEmail.getText().toString());
                    editor.commit();
                }

            }
        });
        btnsignup.setOnClickListener(new View.OnClickListener() {
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

    public void post(){
        showDialog();
        JSONObject obj = new JSONObject();

        try {
            obj.put("email", inputEmail.getText());
            obj.put("password", inputPassword.getText());
            final JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Constant.TOKEN_URL, obj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
//                    textResult.setText(response.toString());
                    hideDialog();
                    textResult.setText("log success");
                    if(response!=null) {
                        try {
                            test = settingsActivity.getString("test", "");
                            textResult.setText(test);
                            SharedPreferences.Editor editor=settingsActivity.edit();
                            editor.putString("test",response.getString("user_id"));
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
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("email", inputEmail.getText().toString());
                    params.put("password", inputPassword.getText().toString());
//                        params.put("User-Agent", );
//                        params.put("Accept-Language", "fr");

                    return params;
                }
            };

            mRequestHelper.addToRequestQueue(req, TAG);
        } catch (JSONException e){
            e.printStackTrace();
        }
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
