package tw.tkusd.appstudio.app;

import com.google.gson.annotations.Expose;

/**
 * Created by wenlin on 2015/7/23.
 */
public class Result {
    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private String email;

    private  String error ;
    private  String message;

    // success response
    public String getId() {return id;}
    public String getName() {return name;}
    public String getEmail(){
        return  email;
    }

    //failure response
    public String geterror(){return error;}
    public String getmessage(){return  message;}

}
