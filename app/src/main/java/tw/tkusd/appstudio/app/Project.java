package tw.tkusd.appstudio.app;

import com.google.gson.annotations.Expose;


/**
 * Created by user on 2015/7/28.
 */
public class Project {
    @Expose
       private String user_id;


    public Project(String user_id){
        this.user_id = user_id;
    }



    public String getUserId() {
        return user_id;
    }

    public void setUser_id(String user_id){this.user_id=user_id;}

}
