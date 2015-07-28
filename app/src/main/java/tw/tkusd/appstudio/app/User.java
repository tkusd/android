package tw.tkusd.appstudio.app;

import com.google.gson.annotations.Expose;

/**
 * Created by wenlin on 2015/7/23.
 */
public class User {
    @Expose
    private String id;
    @Expose
    private String user_id;
    @Expose
    private String name;
    @Expose
    private String email;
    private String password;

    private String error ;
    private String message;
    //signup
    public  User(String name,String email,String password) {
        this.name= name;
        this.email = email;
        this.password = password;
    }
    //token
    public User(String email,String password){
        this.email=email;
        this.password=password;
    }
    // success response
    public String getId() {
        return id;
    }
    public String getUserId(){
        return user_id;
    }
    public String getName() {
        return name;
    }
    public String getEmail(){
        return  email;
    }
    //failure response
    public String geterror(){
        return error;
    }
    public String getmessage(){
        return  message;
    }
}
