package tw.tkusd.diff.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.Response;
import tw.tkusd.diff.R;
import tw.tkusd.diff.api.API;
import tw.tkusd.diff.api.APIService;
import tw.tkusd.diff.model.Project;
import tw.tkusd.diff.model.ProjectList;
import tw.tkusd.diff.util.TokenHelper;
import tw.tkusd.diff.view.RecyclerViewItemClickListener;

/**
 * Created by SkyArrow on 2015/9/9.
 */
public class ProjectListFragment extends Fragment implements RecyclerViewItemClickListener.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String USER_ID = "USER_ID";

    private ProjectListAdapter adapter;
    private List<Project> projectList;
    private APIService api;
    private UUID userId;
    private TokenHelper tokenHelper;
    private boolean created;
    private boolean loading;
    private boolean clearAll;

    @InjectView(R.id.list)
    RecyclerView recyclerView;

    @InjectView(R.id.container)
    SwipeRefreshLayout swipeRefreshLayout;

    public static ProjectListFragment newInstance(UUID userId) {
        ProjectListFragment fragment = new ProjectListFragment();
        Bundle args = new Bundle();

        args.putString(USER_ID, userId.toString());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        Bundle args = getArguments();
        userId = TokenHelper.parseUUID(args.getString(USER_ID, ""));
        api = API.getInstance(getActivity()).getService();
        tokenHelper = TokenHelper.getInstance(getActivity());

        if (userId == null) {
            new AlertDialog.Builder(getActivity())
                    .setMessage("User ID is invalid")
                    .setCancelable(false)
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }

        projectList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_list, container, false);
        ButterKnife.inject(this, view);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(getString(R.string.projects));
        actionBar.setDisplayHomeAsUpEnabled(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        adapter = new ProjectListAdapter(activity, projectList);
        adapter.setHasStableIds(true);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(activity, this));

        swipeRefreshLayout.setOnRefreshListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!created && userId != null) {
            created = true;

            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });

            loadProjectList();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.project_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                showSettings();
                return true;

            case R.id.logout:
                showLogoutConfirmDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View childView, int position) {
        Project project = projectList.get(position);
        if (project == null) return;

        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.frame, ProjectPreviewFragment.newInstance(project.getId()));
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    @Override
    public void onRefresh() {
        clearAll = true;
        loadProjectList();
    }

    private void loadProjectList() {
        if (loading) return;
        loading = true;

        api.getProjectList(userId).enqueue(new Callback<ProjectList>() {
            @Override
            public void onResponse(Response<ProjectList> response) {
                if (!response.isSuccess()) {
                    handleLoadFailed();
                    return;
                }

                handleLoadSuccess(response);
            }

            @Override
            public void onFailure(Throwable t) {
                Logger.e(t, "Project list load failed");
                handleLoadFailed();
            }
        });
    }

    private void handleLoadSuccess(Response<ProjectList> response){
        if (clearAll) {
            clearAll = false;
            projectList.clear();
        }

        projectList.addAll(response.body().getData());
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
        loading = false;
    }

    private void handleLoadFailed(){
        swipeRefreshLayout.setRefreshing(false);
        loading = false;
        clearAll = false;
        showErrorDialog();
    }

    private void showErrorDialog() {
        new AlertDialog.Builder(getActivity())
                .setMessage(getString(R.string.project_list_load_failed))
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    private void showSettings() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.frame, SettingsFragment.newInstance());
        ft.addToBackStack(null);
        ft.commit();
    }

    private void showLogoutConfirmDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.logout))
                .setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(R.string.ok, new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void logout() {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), null, getString(R.string.logging_out), true, false);

        UUID token = tokenHelper.getToken();

        api.deleteToken(token).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Response<Void> response) {
                progressDialog.hide();
                tokenHelper.removeCurrentAccount();

                Intent intent = new Intent(getActivity(), EntryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void onFailure(Throwable t) {
                Logger.e(t, "Logout failed");
                progressDialog.hide();
                showLogoutErrorDialog();
            }
        });
    }

    private void showLogoutErrorDialog() {
        new AlertDialog.Builder(getActivity())
                .setMessage(getString(R.string.logout_failed))
                .setPositiveButton(R.string.ok, null)
                .show();
    }
}
