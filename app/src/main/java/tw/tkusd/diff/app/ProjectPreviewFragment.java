package tw.tkusd.diff.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.xwalk.core.XWalkView;

import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.InjectView;
import tw.tkusd.diff.Constant;
import tw.tkusd.diff.R;
import tw.tkusd.diff.util.TokenHelper;

/**
 * Created by SkyArrow on 2015/9/10.
 */
public class ProjectPreviewFragment extends Fragment {
    private static final String PROJECT_ID = "PROJECT_ID";

    private UUID projectId;

    @InjectView(R.id.web)
    XWalkView webView;

    public static ProjectPreviewFragment newInstance(UUID projectId) {
        ProjectPreviewFragment fragment = new ProjectPreviewFragment();
        Bundle args = new Bundle();

        args.putString(PROJECT_ID, projectId.toString());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        Bundle args = getArguments();
        projectId = TokenHelper.parseUUID(args.getString(PROJECT_ID, ""));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preview, container, false);
        ButterKnife.inject(this, view);

        if (projectId != null) {
            webView.load(String.format(Constant.PREVIEW_URL, projectId.toString()), null);
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(getString(R.string.preview));
        actionBar.setDisplayHomeAsUpEnabled(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
