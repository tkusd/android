package tw.tkusd.appstudio.app;

/**
 * Created by melon on 2015/8/3.
 */
public class Token {

    private String id;
    private String email;
    private String password;

    private String user_id;
    private String error;
    private String message;

    public Token(String email, String password){
        this.email=email;
        this.password=password;
    }

    public String geterror(){
        return error;
    }

    public String getmessage(){
        return  message;
    }

    public String getToken() {
        return id;
    }

    public String getUser_id(){
        return user_id;
    }
}
