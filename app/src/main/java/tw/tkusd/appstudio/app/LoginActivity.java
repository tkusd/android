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

    @InjectView(R.id.btn_send_request)
    Button btnSendRequest;

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

        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String putemail = inputEmail.getText().toString();
                if (!isValidEmail(putemail)) {
                    inputEmail.setError("信箱格式錯誤");
                }
                if (inputPassword.getText().length() == 0) {
                    inputPassword.setError("必填欄位");
                }
                if ( inputPassword.getText().length() != 0 && isValidEmail(putemail)) {
                    login();
                }
            }
        });

        text_signup.setClickable(true);
        text_signup.setFocusable(true);
        text_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        text_help.setClickable(true);
        text_help.setFocusable(true);
        text_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetActivity.class);
                startActivity(intent);
            }
        });
    }

    public  void login() {
        showDialog();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constant.API_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
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
                Toast.makeText(LoginActivity.this, "登入成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, ProjectListActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                hideDialog();

                if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                    nonetdialog();
                } else {
                    String response_error;
                    User user = (User) error.getBodyAs(User.class);
                    response_error = user.geterror();
                    if (response_error.equals("1200")) {
                        inputEmail.setError("信箱不存在");
                    }
                    if (response_error.equals("1300")) {
                        inputPassword.setError("密碼錯誤");
                    }

                }
            }
        });
    }

    private void showDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("請稍後..");
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

    //valid email
    public static boolean isValidEmail(String email){
        if(email==null)
            return false;
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void nonetdialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
        alertDialog.setTitle("失敗");
        alertDialog.setMessage("沒有網路連接");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "確定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }


}
