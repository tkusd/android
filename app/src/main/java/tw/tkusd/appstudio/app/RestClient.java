package tw.tkusd.appstudio.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import tw.tkusd.appstudio.Constant;
import tw.tkusd.appstudio.R;

/**
 * Created by user on 2015/7/31.
 */

/**
 * Created by aleckson on 4/29/2015.
 */
public class RestClient {

    private static API api;
    final Context context;
    Project project;
    TextView txt;
    private SharedPreferences mPref;



    @InjectView(R.id.user_id)
    EditText inputUserID;

    @InjectView(R.id.project_id)
    EditText inputProjectID;

    public RestClient(Context context) {
        this.context = context;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constant.API_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        API api = restAdapter.create(API.class);
        String user_id = mPref.getString(Constant.PREF_USER_ID, "");

        api.projects(user_id, new Callback<Project>() {


            @Override
            public void success(Project project, retrofit.client.Response response) {
                String getuserid = project.getUserId();

                //mPref = PreferenceManager.getDefaultSharedPreferences(ProjectListActivity.this);
                SharedPreferences.Editor editor = mPref.edit();
                editor.putString(Constant.PREF_USER_ID, getuserid);
                editor.apply();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    public API getApi() {
        return api;
    }


}