package tw.tkusd.diff.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
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
import retrofit.Callback;
import retrofit.Response;
import tw.tkusd.diff.R;
import tw.tkusd.diff.api.API;
import tw.tkusd.diff.api.APIService;
import tw.tkusd.diff.model.APIError;
import tw.tkusd.diff.model.Token;
import tw.tkusd.diff.util.TokenHelper;

/**
 * Created by SkyArrow on 2015/9/9.
 */
public class LoginFragment extends LoginBaseFragment {
    private APIService api;
    private TokenHelper tokenHelper;

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

    public static LoginFragment newInstance(){
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        api = API.getInstance(getActivity()).getService();
        tokenHelper = TokenHelper.getInstance(getActivity());
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

    @OnClick(R.id.login)
    void loginPressed(){
        resetTextInputLayouts();
        validator.validate();

        if (!isValidated()) return;

        login(emailText.getText().toString(), passwordText.getText().toString(), new Callback<Token>() {
            @Override
            public void onResponse(Response<Token> response) {
                if (response.isSuccess()) return;

                APIError error = API.parseAPIError(response.errorBody());
                if (error == null) return;

                String field = error.getField();
                String msg = error.getMessage();

                if (field.equals("email")){
                    emailLayout.setError(msg);
                } else if (field.equals("password")){
                    passwordLayout.setError(msg);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                //
            }
        });
    }
}
