package tw.tkusd.appstudio.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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

import butterknife.ButterKnife;
import butterknife.InjectView;
import tw.tkusd.appstudio.Constant;
import tw.tkusd.appstudio.R;
import tw.tkusd.appstudio.util.RequestHelper;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        getSupportActionBar().setTitle(getString(R.string.signup));

        mRequestHelper = RequestHelper.getInstance(this);

        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
            }
        });
    }

    @Override
    protected void onDestroy() {
        mRequestHelper.cancelAllRequests(TAG);
        super.onDestroy();
    }


    public void post(){
        showDialog();
        JSONObject obj = new JSONObject();

        try {
            obj.put("name", inputName.getText());
            obj.put("email", inputEmail.getText());
            obj.put("password", inputPassword.getText());

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Constant.USER_URL, obj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //textResult.setText(response.toString());// 回傳值
                    hideDialog();
                    Toast.makeText(MainActivity.this,"註冊成功",Toast.LENGTH_SHORT).show();

                    //跳轉畫面
                    showDialog();  //
                    Handler mHand=new Handler();
                    mHand.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, Goto.class);
                            hideDialog();//
                            startActivity(intent);
                        }
                    }, 3000);//停留3秒

                    //跳轉end

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                   if (error.networkResponse != null) {
                        try {
                            JSONObject result = new JSONObject(new String(error.networkResponse.data));
                            Toast.makeText(MainActivity.this,result.toString(),Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                       Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                   }
                    hideDialog();
                }
            });

            mRequestHelper.addToRequestQueue(req, TAG);
        } catch (JSONException e){
            e.printStackTrace();
        }

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
