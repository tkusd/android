package tw.tkusd.appstudio.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import tw.tkusd.appstudio.Constant;
import tw.tkusd.appstudio.R;

public class ProjectListActivity extends AppCompatActivity {
    public static final String TAG = ProjectListActivity.class.getSimpleName();
    private List<Project> project ;

    private SharedPreferences mPref;

    @InjectView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private ProjectListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);
        ButterKnife.inject(this);

        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        getList();

        project = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter=new ProjectListAdapter(this,project);

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(ProjectListActivity.this, project.get(position).gettitle(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        // do nothing
                    }
                })
        );

    }

    public void getList() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constant.API_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
       API api = restAdapter.create(API.class);
        String user_id = mPref.getString(Constant.PREF_USER_ID,"");

        api.getlist(user_id, new Callback<ProjectList>() {

            @Override
            public void success(ProjectList projectList , retrofit.client.Response response) {
                project.addAll(projectList.getData());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                String response_error, response_message;
                User user = (User) error.getBodyAs(User.class);
                response_error = user.geterror();
                response_message = user.getmessage();
                Toast.makeText(ProjectListActivity.this, "error:" + response_error + ",message:" + response_message, Toast.LENGTH_SHORT).show();

            }

        });

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

    @OnClick(R.id.setting)
    void onSettingClick() {
        Intent intent=new Intent(ProjectListActivity.this,SettingActivity.class);
        startActivity(intent);
    }

    public void deletetoken(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constant.API_URL).
                        setLogLevel(RestAdapter.LogLevel.FULL).
                        build();
        API api = restAdapter.create(API.class);

        api.deleteToken(mPref.getString(Constant.PREF_TOKEN, ""), new Callback<User>() {

            @Override
            public void success(User user, retrofit.client.Response response) {
                Intent intent = new Intent(ProjectListActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                    nonetdialog();
                } else {
                    String response_error, response_message;
                    User user = (User) error.getBodyAs(User.class);
                    response_error = user.geterror();
                    response_message = user.getmessage();
                    Toast.makeText(ProjectListActivity.this, "error:" + response_error + ",message:" + response_message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void nonetdialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(ProjectListActivity.this).create();
        alertDialog.setTitle("fail");
        alertDialog.setMessage("no network connection");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
