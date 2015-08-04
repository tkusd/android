package tw.tkusd.appstudio.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
    @InjectView(R.id.btn_updatePassword)
    Button btn_updatePassword;
    @InjectView(R.id.btn_deleteAccount)
    Button btn_deleteAccount;


    private SharedPreferences mPref;
    public static final String TAG = SettingActivity.class.getSimpleName();
    private String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.inject(this);
        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        userid = mPref.getString(Constant.PREF_USER_ID, "");
        getUser();

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
                Toast.makeText(SettingActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @OnClick(R.id.btn_deleteAccount)
    void onndeleteclick() {
        new AlertDialog.Builder(SettingActivity.this)
                .setTitle("刪除帳號")
                .setMessage("你確定要刪除帳號嗎?")
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccount();
                    }
                })
                .setNegativeButton("取消", null).show();

    }

    @OnClick(R.id.btn_update)
    void onUpdate(){
        update();
    }

    @OnClick(R.id.btn_updatePassword)
    void onUpdatePassword(){
        updatePassword();
    }

    public void update(){
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
                Toast.makeText(SettingActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        }
//
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
                String response_error;
                User user = (User) error.getBodyAs(User.class);
                response_error = user.geterror();
                if (response_error.equals("1300")) {
                    edtOldpass.setError("invalid password");
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
                Toast.makeText(SettingActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
