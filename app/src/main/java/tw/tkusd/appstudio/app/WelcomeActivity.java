package tw.tkusd.appstudio.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import tw.tkusd.appstudio.Constant;
import tw.tkusd.appstudio.R;
import tw.tkusd.appstudio.util.RequestHelper;

public class WelcomeActivity extends AppCompatActivity {

    @InjectView(R.id.btn_logout)
    Button btnlogout;

//    JSONObject obj = new JSONObject();
    private RequestHelper mRequestHelper;
    private ProgressDialog pDialog;
    public static final String TAG = WelcomeActivity.class.getSimpleName();



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.inject(this);
        mRequestHelper = RequestHelper.getInstance(this);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    post();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void post() throws JSONException {
        showDialog();
        JSONObject obj = new JSONObject();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.DELETE, Constant.ID_URL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideDialog();
                Toast.makeText(WelcomeActivity.this, "登出成功", Toast.LENGTH_SHORT);
                Intent newAct = new Intent();
                newAct.setClass(WelcomeActivity.this,LoginActivity.class);
                startActivity(newAct);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    try {
                        JSONObject result = new JSONObject(new String(error.networkResponse.data));
                        Toast.makeText(WelcomeActivity.this,result.toString(),Toast.LENGTH_SHORT);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                hideDialog();
            }
        });


        mRequestHelper.addToRequestQueue(req, TAG);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_goto, menu);
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
}
