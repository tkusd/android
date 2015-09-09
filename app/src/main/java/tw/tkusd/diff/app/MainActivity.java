package tw.tkusd.diff.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import tw.tkusd.diff.R;
import tw.tkusd.diff.util.TokenHelper;

/**
 * Created by SkyArrow on 2015/9/9.
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = fm.findFragmentById(R.id.frame);
        TokenHelper tokenHelper = TokenHelper.getInstance(this);

        if (fragment != null){
            ft.attach(fragment);
        } else if (tokenHelper.getToken() == null) {
            ft.replace(R.id.frame, LoginPagerFragment.newInstance());
        } else {
            ft.replace(R.id.frame, ProjectListFragment.newInstance(tokenHelper.getUserId()));
        }

        ft.commit();
    }
}
