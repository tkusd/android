package tw.tkusd.diff.app;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import retrofit.Response;
import tw.tkusd.diff.R;
import tw.tkusd.diff.event.LoginEvent;
import tw.tkusd.diff.model.Token;
import tw.tkusd.diff.model.TokenRequest;

/**
 * Created by SkyArrow on 2015/9/10.
 */
public class AuthenticatorActivity extends AppCompatActivity {
    @InjectView(R.id.pager)
    ViewPager viewPager;

    @InjectView(R.id.tabs)
    SmartTabLayout tabLayout;

    private PagerAdapter pagerAdapter;
    private String[] tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticator);
        ButterKnife.inject(this);

        tabs = new String[]{
                getString(R.string.login),
                getString(R.string.signup)
        };

        pagerAdapter = new PagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setViewPager(viewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onEvent(LoginEvent event){
        TokenRequest req = event.getRequest();
        Response<Token> res = event.getResponse();

        if (res != null && res.isSuccess()){
            Intent intent = new Intent();
            intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, req.getEmail());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private class PagerAdapter extends FragmentPagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return SignupFragment.newInstance();
            }

            return LoginFragment.newInstance();
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }

}
