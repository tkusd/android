package tw.tkusd.diff.app;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.inject(this, view);
        validator = new Validator(this);
        validator.setValidationListener(this);

        editTextMap.put(emailText, emailLayout);
        editTextMap.put(passwordText, passwordLayout);

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
        resetTextInputLayouts();
        validator.validate();

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
}
