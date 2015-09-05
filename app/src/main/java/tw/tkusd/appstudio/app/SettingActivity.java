package tw.tkusd.appstudio.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import tw.tkusd.appstudio.Constant;
import tw.tkusd.appstudio.R;

/**
 * Created by melon on 2015/7/28.
 */
public class SettingActivity extends AppCompatActivity {

    @InjectView(R.id.edtName)
    EditText edtName;
    @InjectView(R.id.edtEmail)
    EditText edtEmail;
    @InjectView(R.id.edtOldPass)
    EditText edtOldpass;
    @InjectView(R.id.edtNewPass)
    EditText edtNewPass;
    @InjectView(R.id.edtConfirmPass)
    EditText edtConfirmPass;

    @InjectView(R.id.btn_updateAccount)
    Button btn_updateAccount;
    @InjectView(R.id.btn_deleteAccount)
    Button btn_deleteAccount;
    @InjectView(R.id.btn_updatePassword)
    Button btn_updatePassword;



    private SharedPreferences mPref;
    public static final String TAG = SettingActivity.class.getSimpleName();
    private String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            Window window=getWindow();

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }

        ButterKnife.inject(this);
        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        userid = mPref.getString(Constant.PREF_USER_ID, "");
        getUser();

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        btn_updateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtName.getText().length() == 0) {
                    edtName.setError("name is required");
                }
                final String putemail = edtEmail.getText().toString();
                if (!isValidEmail(putemail)) {
                    edtEmail.setError("invalid email");
                }
                if (edtName.getText().length() != 0 && isValidEmail(putemail)) {
                    updateAccount();
                }

            }
        });

        btn_deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               deletedialog();
            }
        });

        btn_updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtOldpass.getText().length() == 0) {
                    edtOldpass.setError("Password is required.");
                }
                if (edtNewPass.getText().length() < 6) {
                    edtNewPass.setError("Password length need at least 6.");
                }
                String newpass=edtNewPass.getText().toString();
                String confirmpass=edtConfirmPass.getText().toString();
                if(!confirmpass.equals(newpass)){
                    edtConfirmPass.setError("Your password did't match.");
                }
                if(edtOldpass.getText().length() != 0 && edtNewPass.getText().length() >= 6 && confirmpass.equals(newpass)) {
                    updatePassword();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void getUser() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constant.API_URL).setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(new CustomRequestInterceptor(this)).build();
        API api = restAdapter.create(API.class);

        api.getUser(userid, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                String getName = user.getName();
                String getEmail = user.getEmail();
                edtName.setText(getName);
                edtEmail.setText(getEmail);
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                    nonetdialog();
                }else {
                    Toast.makeText(SettingActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void updateAccount(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constant.API_URL).setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(new CustomRequestInterceptor(this)).build();
        API api = restAdapter.create(API.class);
        String name=edtName.getText().toString();
        String email=edtEmail.getText().toString();
        api.updateUser(new User(name, email), userid, new Callback<User>() {

            @Override
            public void success(User user, retrofit.client.Response response) {
                Toast.makeText(SettingActivity.this, "update success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                    nonetdialog();
                } else {
                    Toast.makeText(SettingActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        }

    public void updatePassword(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constant.API_URL).setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(new CustomRequestInterceptor(this)).build();
        API api = restAdapter.create(API.class);
        String email=edtEmail.getText().toString();
        String old_pass=edtOldpass.getText().toString();
        String new_pass=edtNewPass.getText().toString();
        String name=edtName.getText().toString();
        api.updateUser(new User(email, old_pass, new_pass, name), userid, new Callback<User>() {

            @Override
            public void success(User user, retrofit.client.Response response) {
                Toast.makeText(SettingActivity.this, "update success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                    nonetdialog();
                }
                else {
                    User user = (User) error.getBodyAs(User.class);
                    String response_error = user.geterror();
                    String response_message = user.getmessage();

                    if (response_error.equals("1300")) {
                        edtOldpass.setError("wrong password");
                    }else {
                        Toast.makeText(SettingActivity.this, response_message.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void deleteAccount(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constant.API_URL).setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(new CustomRequestInterceptor(this)).build();
        API api = restAdapter.create(API.class);
        String userid = mPref.getString(Constant.PREF_USER_ID, "");
        api.deleteAccount(userid, new Callback<User>() {
            @Override
            public void success(User user, retrofit.client.Response response) {
                Toast.makeText(SettingActivity.this, "delete success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                    nonetdialog();
                }else {
                    Toast.makeText(SettingActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void nonetdialog(){
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(SettingActivity.this).create();
        alertDialog.setTitle("Fail");
        alertDialog.setMessage("No Network Connection.");
        alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void deletedialog(){
        new android.app.AlertDialog.Builder(this).setTitle("message").setMessage("Are you sure to delete account ?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccount();
                    }
                })
                .setNegativeButton("no",null)
                .show();
    }

    public static boolean isValidEmail(String email){
        if(email==null)
            return false;
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
