package tw.tkusd.diff.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by SkyArrow on 2015/9/9.
 */
public class LoginPagerAdapter extends FragmentPagerAdapter {
    private final String tabTitles[] = {"Log in", "Sign up"};

    public LoginPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1:
                return SignupFragment.newInstance();
        }

        return LoginFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
