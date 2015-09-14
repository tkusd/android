package tw.tkusd.diff.app;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SkyArrow on 2015/9/11.
 */
public abstract class ValidatorFragment extends Fragment implements Validator.ValidationListener {
    protected Validator validator;
    protected Map<EditText, TextInputLayout> editTextMap;
    private boolean validated;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editTextMap = new HashMap<>();
    }

    @Override
    public void onValidationSucceeded() {
        validated = true;
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        validated = false;

        for (ValidationError err : errors) {
            View view = err.getView();
            if (view == null || !(view instanceof EditText)) continue;

            EditText editText = (EditText) view;
            if (!editTextMap.containsKey(editText)) continue;

            editTextMap.get(editText).setError(err.getCollatedErrorMessage(getActivity()));
        }
    }

    protected void resetTextInputLayouts() {
        for (TextInputLayout layout : editTextMap.values()) {
            layout.setError(null);
        }
    }

    protected boolean isValidated() {
        return validated;
    }

    protected Validator getValidator(){
        return validator;
    }

    protected void addEditText(EditText editText, TextInputLayout layout){
        editTextMap.put(editText, layout);
    }

    protected void validate(){
        validate(false);
    }

    protected void validate(boolean reset){
        if (reset){
            resetTextInputLayouts();
        }

        validator.validate();
    }
}
