package tw.tkusd.appstudio;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;


public class MainActivity extends AppCompatActivity {

    private EditText mEdtAccount,mEdtPassword;
    private Button mBtnOK;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEdtAccount=(EditText)findViewById(R.id.edtAccount);
        mEdtPassword=(EditText)findViewById(R.id.edtPassword);
        mBtnOK=(Button)findViewById(R.id.btnOK);

        mBtnOK.setOnClickListener(btnOKOnClick);

//        NetworkImageView networkImageView=(NetworkImageView)findViewById(R.id.nivTestView);
//        RequestQueue mQueue= Volley.newRequestQueue(this);
//        ImageLoader imageLoader=new ImageLoader(mQueue,new LruImageCache());
//        networkImageView.setDefaultImageResId(R.drawable.loader_gif);
//        networkImageView.setErrorImageResId(R.drawable.loader_gif);
       // networkImageView.setImageUrl("http://imgarcade.com/1/loading-gif/",imageLoader);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    private View.OnClickListener btnOKOnClick=new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Uri uri= Uri.parse("http://www.google.com");
            Intent it=new Intent(Intent.ACTION_VIEW,uri);
            startActivity(it);
        }
    };
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        return super.onOptionsItemSelected(item);
    }
}
