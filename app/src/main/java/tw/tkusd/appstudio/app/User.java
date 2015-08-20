package tw.tkusd.appstudio.app;

import com.google.gson.annotations.Expose;
/**
 * Created by wenlin on 2015/7/23.
 */


public class User {


    private String user_id;
    private String name;
    private String email;
    private String old_password;
    private String password;

    private String error ;
    private String message;
    //signup
    public  User(String name,String email,String password) {
        this.name= name;
        this.email = email;
        this.password = password;
    }
    //updateUser
    public User(String name, String email){
        this.email=email;
        this.name=name;
    }

    public User(String email,String old_password,String password,String name){
        this.name=name;
        this.email=email;
        this.old_password=old_password;
        this.password=password;
    }

    // success response

    public String getId(){
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
