package tw.tkusd.diff.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import tw.tkusd.diff.R;

/**
 * Created by SkyArrow on 2015/9/9.
 */
public class LoginPagerFragment extends Fragment {
    @InjectView(R.id.pager)
    ViewPager viewPager;

    @InjectView(R.id.tabs)
    SmartTabLayout tabLayout;

    private LoginPagerAdapter pagerAdapter;

    public static LoginPagerFragment newInstance(){
        return new LoginPagerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_pager, container, false);
        ButterKnife.inject(this, view);

        pagerAdapter = new LoginPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setViewPager(viewPager);

        return view;
    }
}
