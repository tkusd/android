package tw.tkusd.appstudio.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        mRequestHelper = RequestHelper.getInstance(this);
        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textResult.setText("");
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
                    showDialog();
                    Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                    hideDialog();//
                    startActivity(intent);
                    //跳轉end
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null) {
                        try {
                            JSONObject result = new JSONObject(new String(error.networkResponse.data));
                            //Toast.makeText(MainActivity.this,result.toString(),Toast.LENGTH_LONG).show();
                            if(inputName.getText().length()==0)
                            {
                                inputName.setError("name is reqired.");
                            }

                            //測email
                            if (inputEmail.getText().length()==0 ) {
                                inputEmail.setError("email is required");
                            }

                            String test = result.getString("message");
                            if(test.equals("Email has been used.")){
                                inputEmail.setError("eamil has been used");
                            }else if(test.equals(("Email is invalid."))){
                                inputEmail.setError("invalid email");
                            }

                            //測password
                            if(inputPassword.getText().length()<6)
                            {
                                inputPassword.setError("password need  at least 6");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        //沒網路時的dialog
                        nonetdialog();
                    }
                    hideDialog();
                }
            });

            mRequestHelper.addToRequestQueue(req, TAG);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    //  沒網路時產生的dialog
    private void nonetdialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("登入失敗");
        alertDialog.setMessage("無網路連接");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
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
