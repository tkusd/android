package tw.tkusd.appstudio.app;

/**
 * Created by wenlin on 2015/7/23.
 */
public class User {
    private String name;
    private String email;
    private String password;

    public User(){};

    public  User(String name, String email,String password) {
        this.name= name;
        this.email = email;
        this.password = password;
    }
    public String getUser() {
        return name;
    }

    public String getEmail() {return email;}

    public String getPassword() {
        return password;
    }
}
