package tw.tkusd.diff.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SkyArrow on 2015/9/9.
 */
public class APIError {
    @SerializedName("error") private int code;
    private String field;
    private String message;

    public int getCode() {
        return code;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
