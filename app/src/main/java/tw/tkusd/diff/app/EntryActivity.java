package tw.tkusd.diff.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import tw.tkusd.diff.util.TokenHelper;

/**
 * Created by SkyArrow on 2015/9/9.
 */
public class EntryActivity extends AppCompatActivity {
    public static final int AUTH_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TokenHelper tokenHelper = TokenHelper.getInstance(this);

        if (tokenHelper.isLoggedIn()){
            startMainActivity();
        } else {
            Intent intent = new Intent(this, AuthenticatorActivity.class);
            startActivityForResult(intent, AUTH_RESULT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTH_RESULT && resultCode == RESULT_OK){
            startMainActivity();
        }
    }

    private void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
