package tw.tkusd.appstudio.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.inject(this);
        SharedPreferences remdname=getPreferences(Activity.MODE_PRIVATE);
        String email_str=remdname.getString("email", "");
        inputEmail.setText(email_str);
        login_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferences remdname=getPreferences(Activity.MODE_PRIVATE);
                    SharedPreferences.Editor edit=remdname.edit();
                    edit.putString("email", inputEmail.getText().toString());
                    edit.commit();
                }
                else{
                    SharedPreferences remdname=getPreferences(Activity.MODE_PRIVATE);
                    SharedPreferences.Editor edit=remdname.edit();
                    edit.putString("email", "");
                    edit.putString("pass", "");
                    edit.commit();
                }
            }
        });
        mRequestHelper = RequestHelper.getInstance(this);
        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
                if(login_check.isChecked()){
                    SharedPreferences remdname=getPreferences(Activity.MODE_PRIVATE);
                    SharedPreferences.Editor edit=remdname.edit();
                    edit.putString("name", inputEmail.getText().toString());
                    edit.commit();
                }
                Intent newAct = new Intent();
                newAct.setClass(LoginActivity.this, WelcomeActivity.class);
                startActivity(newAct);
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
