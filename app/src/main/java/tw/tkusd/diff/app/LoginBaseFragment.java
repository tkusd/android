package tw.tkusd.diff.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.Response;
import tw.tkusd.diff.R;
import tw.tkusd.diff.api.API;
import tw.tkusd.diff.api.APIService;
import tw.tkusd.diff.event.LoginEvent;
import tw.tkusd.diff.model.Token;
import tw.tkusd.diff.model.TokenRequest;
import tw.tkusd.diff.util.TokenHelper;

/**
 * Created by SkyArrow on 2015/9/9.
 */
public abstract class LoginBaseFragment extends ValidatorFragment {
    private APIService api;
    private TokenHelper tokenHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        api = API.getInstance(getActivity()).getService();
        tokenHelper = TokenHelper.getInstance(getActivity());
    }

    protected APIService getAPI() {
        return api;
    }

    protected void login(final String email, String password) {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), null, getString(R.string.logging_in), true, false);
        final TokenRequest req = new TokenRequest(email, password);

        api.createToken(req).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Response<Token> response) {
                progressDialog.hide();

                if (!response.isSuccess()) {
                    showLoginErrorDialog();
                    EventBus.getDefault().post(new LoginEvent(req, response));
                    return;
                }

                tokenHelper.setAccount(email, response.body());
                EventBus.getDefault().post(new LoginEvent(req, response));
            }

            @Override
            public void onFailure(Throwable t) {
                Logger.e(t, "Login failed");
                progressDialog.hide();
                showLoginErrorDialog();
                EventBus.getDefault().post(new LoginEvent(t));
            }
        });
    }

    private void showLoginErrorDialog() {
        new AlertDialog.Builder(getActivity())
                .setMessage(getString(R.string.login_failed))
                .setPositiveButton(R.string.ok, null)
                .show();
    }
}
