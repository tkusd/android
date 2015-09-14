package tw.tkusd.diff.model;

/**
 * Created by SkyArrow on 2015/9/14.
 */
public class ResetPasswordRequest {
    private String email;

    public ResetPasswordRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
