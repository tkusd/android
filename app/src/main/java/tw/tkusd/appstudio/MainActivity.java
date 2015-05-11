package tw.tkusd.appstudio;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;


public class MainActivity extends AppCompatActivity {

    private EditText mEdtID,mEdtPassword;
    private Button mBtnOK;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEdtID=(EditText)findViewById(R.id.edtID);
        mEdtPassword=(EditText)findViewById(R.id.edtPassword);
        mBtnOK=(Button)findViewById(R.id.btnOK);

        mBtnOK.setOnClickListener(btnOKOnClick);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
