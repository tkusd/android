package tw.tkusd.appstudio.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import tw.tkusd.appstudio.Constant;
import tw.tkusd.appstudio.R;
import tw.tkusd.appstudio.network.APIStringRequest;
import tw.tkusd.appstudio.util.RequestHelper;

public class ProjectListActivity extends AppCompatActivity {
    public static final String TAG = ProjectListActivity.class.getSimpleName();

    private RequestHelper mRequestHelper;
    private SharedPreferences mPref;
    String taketoken_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);
        ButterKnife.inject(this);

        mRequestHelper = RequestHelper.getInstance(this);
        mPref = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    protected void onDestroy() {
        mRequestHelper.cancelAllRequests(TAG);

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_project_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.logout)
    void onLogoutClick() {
        deletetoken();
    }

    public void loadPreference() {
        int mode = Activity.MODE_PRIVATE;
        SharedPreferences mySharedPreference = getSharedPreferences(SignupActivity.MYPREFS, mode);
        taketoken_id = mySharedPreference.getString("tokenid", "null");
    }
    public void deletetoken(){
        loadPreference();
        String API = Constant.USER_URL ;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API).
                        setLogLevel(RestAdapter.LogLevel.FULL).
                        build();
        gitapi git = restAdapter.create(gitapi.class);

        git.delete(taketoken_id,new Callback<Result>() {

            @Override
            public void success(Result result, retrofit.client.Response response) {
                Intent intent = new Intent(ProjectListActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            @Override
            public void failure(RetrofitError error) {
                if (error.isNetworkError()) {
                    nonetdialog();
                } else {
                    String response_error, response_message;
                    Result result = (Result) error.getBodyAs(Result.class);
                    response_error = result.geterror();
                    response_message = result.getmessage();
                    Toast.makeText(ProjectListActivity.this, "error:" + response_error + ",message:" + response_message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void nonetdialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(ProjectListActivity.this).create();
        alertDialog.setTitle("fail");
        alertDialog.setMessage("no network connection");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
