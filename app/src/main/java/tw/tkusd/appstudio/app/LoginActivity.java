package tw.tkusd.appstudio.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import tw.tkusd.appstudio.Constant;
import tw.tkusd.appstudio.R;
import tw.tkusd.appstudio.network.APIObjectRequest;
import tw.tkusd.appstudio.util.RequestHelper;

/**
 * Created by melon on 2015/6/7.
 */
public class LoginActivity extends AppCompatActivity{
    public static final String TAG = LoginActivity.class.getSimpleName();

    @InjectView(R.id.email)
    EditText inputEmail;

    @InjectView(R.id.password)
    EditText inputPassword;

    @InjectView(R.id.result)
    TextView textResult;

    private RequestHelper mRequestHelper;
    private ProgressDialog pDialog;
    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        mRequestHelper = RequestHelper.getInstance(this);
        mPref = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    protected void onDestroy() {
        mRequestHelper.cancelAllRequests(TAG);
        super.onDestroy();
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

    @OnClick(R.id.btn_send_request)
    void login() {
        showDialog();

        try {
            JSONObject obj = new JSONObject();
            obj.put("email", inputEmail.getText());
            obj.put("password", inputPassword.getText());

            APIObjectRequest req = new APIObjectRequest(this, Request.Method.POST, "http://tkusd.zespia.tw/v1/tokens", obj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    SharedPreferences.Editor editor = mPref.edit();

                    try {
                        editor.putString(Constant.PREF_TOKEN, response.getString("id"));
                        editor.putString(Constant.PREF_USER_ID, response.getString("user_id"));
                        editor.apply();

                        hideDialog();
                        Intent intent = new Intent(LoginActivity.this, ProjectListActivity.class);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        hideDialog();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    hideDialog();
                }
            });

            mRequestHelper.addToRequestQueue(req, TAG);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_signup)
    void signup() {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }
}
