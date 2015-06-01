package tw.tkusd.appstudio.app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import tw.tkusd.appstudio.Constant;
import tw.tkusd.appstudio.R;
import tw.tkusd.appstudio.util.RequestHelper;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private Button btnSendRequest;
    private TextView mText;
    private RequestHelper mRequestHelper;
    private ProgressDialog pDialog;
    private EditText txtname, txtemail,txtpassword;
    private String username,useremail,userpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRequestHelper = RequestHelper.getInstance(this);

        btnSendRequest = (Button) findViewById(R.id.btn_send_request);

        txtname = (EditText) findViewById(R.id.name);//取得ed姓名
        txtemail=(EditText) findViewById(R.id.email);//取得ed eamil
        txtpassword = (EditText) findViewById(R.id.password);//取得ed 密碼


        mText = (TextView) findViewById(R.id.Text);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = txtname.getText().toString();
                useremail=txtemail.getText().toString();
                userpassword = txtpassword.getText().toString();

                post();

            }
        });
    }

    @Override
    protected void onDestroy() {
        mRequestHelper.cancelAllRequests(TAG);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
   public void post(){
       showpDialog();
       JSONObject obj = new JSONObject();
       try {
           obj.put("name", username);
           obj.put("email", useremail);
           obj.put("password",userpassword);

           JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Constant.POST_URL, obj, new Response.Listener<JSONObject>() {
               @Override
               public void onResponse(JSONObject response) {
                   //mText.setText(response.toString());// 回傳值
                   hidepDialog();
                   mText.setText("註冊成功");

               }
           }, new Response.ErrorListener() {
               @Override
               public void onErrorResponse(VolleyError error) {
                   mText.setText(error.toString());

                   if (error.networkResponse != null) {
                       try {
                           JSONObject result = new JSONObject(new String(error.networkResponse.data));
                           mText.setText(result.toString());
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }

                   }
                   hidepDialog();
               }
           });

           mRequestHelper.addToRequestQueue(req, TAG);
       } catch (JSONException e) {
           e.printStackTrace();

       }


   }
    //method
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
