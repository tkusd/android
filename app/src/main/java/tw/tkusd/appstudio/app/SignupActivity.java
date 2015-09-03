package tw.tkusd.appstudio.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import tw.tkusd.appstudio.Constant;
import tw.tkusd.appstudio.R;

public class SignupActivity extends AppCompatActivity {
    public static final String TAG = SignupActivity.class.getSimpleName();

    @InjectView(R.id.btn_send_request)
    Button btnSendRequest;

    @InjectView(R.id.name)
    EditText inputName;

    @InjectView(R.id.email)
    EditText inputEmail;

    @InjectView(R.id.password)
    EditText inputPassword;

    @InjectView(R.id.login)
    TextView login;

    private ProgressDialog pDialog;
    private SharedPreferences mPref;
    private Context mContext;
    private boolean isok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            Window window=getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }

        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String putemail = inputEmail.getText().toString();
                if (!isValidEmail(putemail)) {
                    inputEmail.setError("invalid email");
                }
                if (inputName.getText().length() == 0) {
                    inputName.setError("name  is required");
                }
                if (inputPassword.getText().length() < 6) {
                    inputPassword.setError("password length need at least 6");
                }
                if (inputName.getText().length() != 0 && inputPassword.getText().length() != 0 && isValidEmail(putemail)) {
                    signup();
                }
            }
        });
        login.setClickable(true);
        login.setFocusable(true);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);

                 }
        });
    }

    public void signup() {
        showDialog();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constant.API_URL).
                        setLogLevel(RestAdapter.LogLevel.FULL).
                        build();
        API api = restAdapter.create(API.class);

        final String name=inputName.getText().toString();
        final String email=inputEmail.getText().toString();
        String password=inputPassword.getText().toString();
        api.signup(new User(name, email, password), new Callback<User>() {

            @Override
            public void success(User user, retrofit.client.Response response) {
                mPref = PreferenceManager.getDefaultSharedPreferences(SignupActivity.this);
                SharedPreferences.Editor editor = mPref.edit();
                editor.putString(Constant.PREF_NAME, name);
                editor.putString(Constant.PREF_EMAIL, email);
                editor.apply();
                login();

            }

            @Override
            public void failure(RetrofitError error) {
                hideDialog();
                //no network
                if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                    nonetdialog();
                } else {
                    //email used
                    String response_error;
                    User user = (User) error.getBodyAs(User.class);
                    response_error = user.geterror();
                    if (response_error.equals("1301")) {
                        inputEmail.setError("email has been  used");
                    }

                }
            }
        });
    }

    public void login() {
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

                mPref = PreferenceManager.getDefaultSharedPreferences(SignupActivity.this);
                SharedPreferences.Editor editor = mPref.edit();
                editor.putString(Constant.PREF_TOKEN, gettoken);
                editor.putString(Constant.PREF_USER_ID, getuserid);
                editor.apply();
                Toast.makeText(SignupActivity.this, "註冊成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignupActivity.this, ProjectListActivity.class);
                startActivity(intent);
            }
            @Override
            public void failure(RetrofitError error) {
                hideDialog();
                Toast.makeText(SignupActivity.this,error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    //  沒網路時產生的dialog
    private void nonetdialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(SignupActivity.this).create();
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
    //valid email
    public static boolean isValidEmail(String email){
        if(email==null)
            return false;
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
