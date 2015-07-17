package tw.tkusd.appstudio.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

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
    public static final String MYPREFS="mySharedPreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.inject(this);
        mRequestHelper = RequestHelper.getInstance(this);

        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
                // try {
                //  test();
                //} catch (JSONException e) {
                //   e.printStackTrace();
                //     }
            }
        });
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newAct = new Intent();
                newAct.setClass(LoginActivity.this, MainActivity.class);
                startActivity(newAct);
                finish();
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
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", inputEmail.getText().toString());
        params.put("password", inputPassword.getText().toString());
        final CustomReq req = new CustomReq(Request.Method.POST, Constant.TOKEN_URL, params, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                hideDialog();
                if(response!=null) {
                    try {
                        Toast.makeText(LoginActivity.this, "login success", Toast.LENGTH_SHORT).show();
                        //sharedpreference----------------------------------------------------------
                        int mode=Activity.MODE_PRIVATE;
                        SharedPreferences mySharedPreference=getSharedPreferences(MYPREFS, mode);
                        SharedPreferences.Editor editor = mySharedPreference.edit();
                        editor.putBoolean("isTrue", true);
                        String gettokenid = response.getString("id");
                        editor.putString("tokenid", gettokenid);
                        editor.commit();
                        //------------------------------------------------------------------------

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
                if (error.networkResponse != null) {
                    try {
                        JSONObject result = new JSONObject(new String(error.networkResponse.data));
                        textResult.setText(result.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    //no network dialog
                    nonetdialog();
                }
                hideDialog();
            }

        }){

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
    private void nonetdialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
        alertDialog.setTitle("fail");
        alertDialog.setMessage("no network");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
