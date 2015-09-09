package tw.tkusd.diff.model;

/**
 * Created by SkyArrow on 2015/9/9.
 */
public class TokenRequest {
    private String email;
    private String password;

    public TokenRequest(String email, String password){
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
