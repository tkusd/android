package tw.tkusd.appstudio.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
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

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.mime.TypedByteArray;
import tw.tkusd.appstudio.Constant;
import tw.tkusd.appstudio.R;
import tw.tkusd.appstudio.network.APIObjectRequest;
import tw.tkusd.appstudio.util.RequestHelper;

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

    @InjectView(R.id.result)
    TextView textResult;

    private RequestHelper mRequestHelper;
    private ProgressDialog pDialog;
    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);
        mRequestHelper = RequestHelper.getInstance(this);
        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textResult.setText("");
                singup();

            }
        });
    }

    @Override
    protected void onDestroy() {
        mRequestHelper.cancelAllRequests(TAG);
        super.onDestroy();
    }

    public void singup() {
        showDialog();
        String API = Constant.USER_URL;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API).
                        setLogLevel(RestAdapter.LogLevel.FULL).
                        build();
        gitapi git = restAdapter.create(gitapi.class);

        String name=inputName.getText().toString();
        String email=inputEmail.getText().toString();
        String password=inputPassword.getText().toString();
        git.login(new User(name,email,password), new Callback<Result>() {

            @Override
            public void success(Result result, retrofit.client.Response response) {
                hideDialog();
                Toast.makeText(SignupActivity.this, "sing up seccess", Toast.LENGTH_SHORT).show();
                //跳轉start
                Intent intent = new Intent(SignupActivity.this, ProjectListActivity.class);
                startActivity(intent);
                //跳轉end
            }

            @Override
            public void failure(RetrofitError error) {
                hideDialog();
                //no network
                if (error.isNetworkError()) {
                    nonetdialog();
                } else {
                    //email
                    String response_error,response_message;
                    Result result = (Result) error.getBodyAs(Result.class);
                    response_error = result.geterror();
                    //response_message=result.getmessage();    測message
                    if (response_error.equals("1301")) {
                        inputEmail.setError("email has been  used");
                    }
                    final String putemail = inputEmail.getText().toString();
                    if (!isValidEmail(putemail)) {
                        inputEmail.setError("invalid email");
                    }
                    //name
                    if (inputName.getText().length() == 0) {
                        inputName.setError("name  is required");
                    }
                    //password
                    if (inputPassword.getText().length() < 6) {
                        inputPassword.setError("password need at least 6");
                    }
                }
            }
        });
    }

    //  沒網路時產生的dialog
    private void nonetdialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(SignupActivity.this).create();
        alertDialog.setTitle("註冊失敗");
        alertDialog.setMessage("無網路連接");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
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
