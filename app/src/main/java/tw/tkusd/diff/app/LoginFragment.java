package tw.tkusd.diff.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.Response;
import tw.tkusd.diff.R;
import tw.tkusd.diff.api.API;
import tw.tkusd.diff.event.LoginEvent;
import tw.tkusd.diff.model.APIError;
import tw.tkusd.diff.model.ResetPasswordRequest;
import tw.tkusd.diff.model.Token;

/**
 * Created by SkyArrow on 2015/9/9.
 */
public class LoginFragment extends LoginBaseFragment {
    @InjectView(R.id.email_layout)
    TextInputLayout emailLayout;

    @NotEmpty
    @Email
    @InjectView(R.id.email)
    EditText emailText;

    @InjectView(R.id.password_layout)
    TextInputLayout passwordLayout;

    @NotEmpty
    @Length(min = 6, max = 50)
    @InjectView(R.id.password)
    EditText passwordText;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.inject(this, view);
        validator = new Validator(this);
        validator.setValidationListener(this);

        addEditText(emailText, emailLayout);
        addEditText(passwordText, passwordLayout);

        passwordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) || actionId == EditorInfo.IME_ACTION_DONE){
                    loginPressed();
                    return true;
                }

                return false;
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @OnClick(R.id.login)
    void loginPressed() {
        validate(true);
        if (!isValidated()) return;

        login(emailText.getText().toString(), passwordText.getText().toString());
    }

    public void onEvent(LoginEvent event){
        Response<Token> res = event.getResponse();
        if (res == null || res.isSuccess()) return;

        APIError err = API.parseAPIError(res.errorBody());
        if (err == null) return;

        String field = err.getField();
        String msg = err.getMessage();

        if (TextUtils.equals(field, "email")) {
            emailLayout.setError(msg);
        } else if (TextUtils.equals(field, "password")) {
            passwordLayout.setError(msg);
        }
    }

    @OnClick(R.id.reset_password)
    void showResetPasswordDialog(){
        final EditText text = new EditText(getActivity());
        text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.reset_password)
                .setMessage(R.string.reset_password_msg)
                .setView(text)
                .setPositiveButton(R.string.ok, new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestResetPassword(text.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void requestResetPassword(String email){
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), null, getString(R.string.loading), true, false);

        getAPI().resetPassword(new ResetPasswordRequest(email)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Response<Void> response) {
                progressDialog.hide();

                if (!response.isSuccess()){
                    showResetPasswordFailedDialog();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Logger.e(t, "Reset failed");
                progressDialog.hide();
                showResetPasswordFailedDialog();
            }
        });
    }

    private void showResetPasswordFailedDialog(){
        new AlertDialog.Builder(getActivity())
                .setMessage(R.string.reset_password_failed)
                .setPositiveButton(R.string.ok, null)
                .show();
    }
}
