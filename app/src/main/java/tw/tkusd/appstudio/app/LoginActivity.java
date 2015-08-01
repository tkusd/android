package tw.tkusd.appstudio.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import tw.tkusd.appstudio.Constant;
import tw.tkusd.appstudio.R;
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
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constant.API_URL).
                        setLogLevel(RestAdapter.LogLevel.FULL).
                        build();
        API api = restAdapter.create(API.class);
        String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();
        api.login(new User(email, password), new Callback<User>() {
            @Override
            public void success(User user, retrofit.client.Response response) {
                String gettoken = user.getId();
                String getuserid = user.getUserId();

                mPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                SharedPreferences.Editor editor = mPref.edit();
                editor.putString(Constant.PREF_TOKEN, gettoken);
                editor.putString(Constant.PREF_USER_ID, getuserid);
                editor.apply();

                hideDialog();
                Toast.makeText(LoginActivity.this, "login success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, ProjectListActivity.class);
                startActivity(intent);
            }
            @Override
            public void failure(RetrofitError error) {
                hideDialog();
                String response_error;
                User user = (User) error.getBodyAs(User.class);
                response_error = user.geterror();
                if (response_error.equals("1100")) {
                    inputEmail.setError("invalid email");
                    inputPassword.setError("invalid password");
                }
                if(response_error.equals("1300")){
                    inputPassword.setError("invalid password");
                }
                if(response_error.equals("1200")){
                    inputEmail.setError("email hasn't been used");
                }
                if(response_error.equals("1104")){
                    inputEmail.setError("invalid email");
                }

            }
        });
    }

    @OnClick(R.id.btn_signup)
    void signup() {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }
}
