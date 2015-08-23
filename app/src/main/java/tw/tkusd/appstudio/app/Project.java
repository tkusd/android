package tw.tkusd.appstudio.app;

import com.google.gson.annotations.Expose;


/**
 * Created by user on 2015/7/28.
 */
public class Project {
    @Expose
    private String title;

    public Project(String title){
        this.title = title;
    }

    public String gettitle() {
        return title;
    }

}
