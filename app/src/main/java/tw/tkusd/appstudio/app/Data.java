package tw.tkusd.appstudio.app;

import com.google.gson.annotations.Expose;

/**
 * Created by melon on 2015/7/31.
 */
public class Data {
    @Expose
    private String name;
    @Expose
    private String email;
    private String password;
    private String old_password;

    private String error ;
    private String message;

    public Data(String name,String email){
        this.email=email;
        this.name=name;
    }
    public  Data(String email,String old_password,String password){
        this.email=email;
        this.old_password=old_password;
        this.password=password;
    }
    // success response
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
