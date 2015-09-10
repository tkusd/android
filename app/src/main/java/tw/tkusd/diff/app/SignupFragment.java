package tw.tkusd.diff.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import tw.tkusd.diff.model.APIError;
import tw.tkusd.diff.model.Token;
import tw.tkusd.diff.model.User;

/**
 * Created by SkyArrow on 2015/9/9.
 */
public class SignupFragment extends LoginBaseFragment {
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

    @InjectView(R.id.password_layout)
    TextInputLayout passwordLayout;

    @NotEmpty
    @Length(min = 6, max = 50)
    @InjectView(R.id.password)
    EditText passwordText;

    public static SignupFragment newInstance() {
        return new SignupFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.inject(this, view);

        validator = new Validator(this);
        validator.setValidationListener(this);

        editTextMap.put(nameText, nameLayout);
        editTextMap.put(emailText, emailLayout);
        editTextMap.put(passwordText, passwordLayout);

        return view;
    }

    @OnClick(R.id.signup)
    void signup() {
        resetTextInputLayouts();
        validator.validate();

        if (!isValidated()) return;

        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), null, getString(R.string.signing_up), true, false);
        final User user = new User();

        user.setName(nameText.getText().toString());
        user.setEmail(emailText.getText().toString());
        user.setPassword(passwordText.getText().toString());

        getAPI().createUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response) {
                progressDialog.hide();

                if (!response.isSuccess()) {
                    APIError error = API.parseAPIError(response.errorBody());

                    if (error != null) {
                        String field = error.getField();
                        String msg = error.getMessage();

                        if (TextUtils.equals(field, "name")) {
                            nameLayout.setError(msg);
                        } else if (TextUtils.equals(field, "email")) {
                            emailLayout.setError(msg);
                        } else if (TextUtils.equals(field, "password")) {
                            passwordLayout.setError(msg);
                        } else {
                            showErrorDialog();
                        }
                    } else {
                        showErrorDialog();
                    }

                    return;
                }

                login(user.getEmail(), user.getPassword());
            }

            @Override
            public void onFailure(Throwable t) {
                Logger.e(t, "Signup failed");
                progressDialog.hide();
                showErrorDialog();
            }
        });
    }

    private void showErrorDialog() {
        new AlertDialog.Builder(getActivity())
                .setMessage(getString(R.string.signup_failed))
                .setPositiveButton(R.string.ok, null)
                .show();
    }
}
