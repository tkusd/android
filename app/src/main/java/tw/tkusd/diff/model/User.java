package tw.tkusd.diff.model;

/**
 * Created by SkyArrow on 2015/9/9.
 */
public class User extends BaseModel {
    private String name;
    private String email;
    private String avatar;
    private String language;
    private String password;

    public String getAvatar() {
        return avatar;
    }

    public String getEmail() {
        return email;
    }

    public String getLanguage() {
        return language;
    }

    public String getName() {
        return name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
