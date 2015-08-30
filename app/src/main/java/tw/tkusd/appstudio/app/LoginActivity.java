package tw.tkusd.appstudio.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

/**
 * Created by melon on 2015/6/7.
 */
public class LoginActivity extends AppCompatActivity{
    public static final String TAG = LoginActivity.class.getSimpleName();

    @InjectView(R.id.email)
    EditText inputEmail;

    @InjectView(R.id.password)
    EditText inputPassword;

    @InjectView(R.id.text_signup)
    TextView text_signup;
    @InjectView(R.id.text_help)
    TextView text_help;

    private ProgressDialog pDialog;
    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //set  status bar color
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            Window window=getWindow();

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }

        ButterKnife.inject(this);
        mPref = PreferenceManager.getDefaultSharedPreferences(this);

        text_signup.setClickable(true);
        text_signup.setFocusable(true);
        text_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
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
        api.login(new Token(email, password), new Callback<Token>() {
            @Override
            public void success(Token token, retrofit.client.Response response) {
                String gettoken = token.getToken();
                String getuserid = token.getUser_id();

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

                if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                    nonetdialog();
                }else {
                    String response_error;
                    User user = (User) error.getBodyAs(User.class);
                    response_error = user.geterror();
                    if (response_error.equals("1100")) {
                        inputEmail.setError("invalid email");
                        inputPassword.setError("invalid password");
                    }
                    if (response_error.equals("1300")) {
                        inputPassword.setError("invalid password");
                    }
                    if (response_error.equals("1200")) {
                        inputEmail.setError("email didn't exist");
                    }
                    if (response_error.equals("1104")) {
                        inputEmail.setError("invalid email");
                    }
                }
            }
        });
    }

    private void nonetdialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
        alertDialog.setTitle("Fail");
        alertDialog.setMessage("No Network Connection.");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }


}
