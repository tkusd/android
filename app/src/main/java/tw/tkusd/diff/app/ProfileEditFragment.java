package tw.tkusd.diff.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import tw.tkusd.diff.R;
import tw.tkusd.diff.api.API;
import tw.tkusd.diff.api.APIService;
import tw.tkusd.diff.model.User;
import tw.tkusd.diff.util.TokenHelper;

/**
 * Created by SkyArrow on 2015/9/11.
 */
public class ProfileEditFragment extends ValidatorFragment {
    private APIService api;
    private TokenHelper tokenHelper;

    @InjectView(R.id.name_layout)
    TextInputLayout nameLayout;

    @NotEmpty
    @Length(max = 100)
    @InjectView(R.id.name)
    EditText nameText;

    @InjectView(R.id.email_layout)
    TextInputLayout emailLayout;

    @NotEmpty
    @Email
    @InjectView(R.id.email)
    EditText emailText;

    @InjectView(R.id.old_password_layout)
    TextInputLayout oldPasswordLayout;

    @InjectView(R.id.old_password)
    EditText oldPasswordText;

    @InjectView(R.id.password_layout)
    TextInputLayout passwordLayout;

    @InjectView(R.id.password)
    EditText passwordText;

    public static ProfileEditFragment newInstance(){
        return new ProfileEditFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        api = API.getInstance(getActivity()).getService();
        tokenHelper = TokenHelper.getInstance(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_edit, container, false);
        ButterKnife.inject(this, view);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(getString(R.string.edit_profile));
        actionBar.setDisplayHomeAsUpEnabled(true);

        validator = new Validator(this);
        validator.setValidationListener(this);

        addEditText(nameText, nameLayout);
        addEditText(emailText, emailLayout);
        addEditText(oldPasswordText, oldPasswordLayout);
        addEditText(passwordText, passwordLayout);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null){
            loadUser();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.update:
                updateUser();
                return true;

            case android.R.id.home:
                popBackStack();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        // Hide keyboard
        View view = getActivity().getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        super.onPause();
    }

    private void loadUser(){
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), null, getString(R.string.loading), true, false);

        api.getUser(tokenHelper.getUserId()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response) {
                progressDialog.hide();

                if (!response.isSuccess()) {
                    showLoadErrorDialog();
                    return;
                }

                User user = response.body();
                nameText.setText(user.getName());
                emailText.setText(user.getEmail());
            }

            @Override
            public void onFailure(Throwable t) {
                Logger.e(t, "User load failed");
                progressDialog.hide();
                showLoadErrorDialog();
            }
        });
    }

    private void showLoadErrorDialog(){
        new AlertDialog.Builder(getActivity())
                .setMessage(getString(R.string.load_failed))
                .setPositiveButton(getString(R.string.retry), new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadUser();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        popBackStack();
                    }
                })
                .show();
    }

    private void updateUser(){
        validate(true);
        if (!isValidated()) return;

        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), null, getString(R.string.updating), true, false);
        User body = new User();
        body.setName(nameText.getText().toString());
        body.setEmail(emailText.getText().toString());

        api.updateUser(tokenHelper.getUserId(), body).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response) {
                progressDialog.hide();

                if (!response.isSuccess()) {
                    showUpdateFailedDialog();
                    return;
                }

                //User newUser = response.body();
                popBackStack();
            }

            @Override
            public void onFailure(Throwable t) {
                Logger.e(t, "Update failed");
                progressDialog.hide();
                showUpdateFailedDialog();
            }
        });
    }

    private void showUpdateFailedDialog(){
        new AlertDialog.Builder(getActivity())
                .setMessage(getString(R.string.update_failed))
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    @OnClick(R.id.delete_account)
    void showDeleteAccountConfirmDialog(){
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.delete_account))
                .setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(R.string.ok, new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccount();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void deleteAccount(){
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), null, getString(R.string.deleting_account), true, false);

        api.deleteUser(tokenHelper.getUserId()).enqueue(new Callback<Void>() {
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
                Logger.e(t, "Delete failed");
                progressDialog.hide();
                showDeleteFailedDialog();
            }
        });
    }

    private void showDeleteFailedDialog(){
        new AlertDialog.Builder(getActivity())
                .setMessage(getString(R.string.delete_failed))
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    private void popBackStack(){
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
