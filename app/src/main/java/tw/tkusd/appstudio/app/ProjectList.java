package tw.tkusd.appstudio.app;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by user on 2015/8/4.
 */
public class ProjectList {
    @Expose
    List<Project> data;

    public ProjectList(List<Project> data) {
        this.data = data;
    }

    public List<Project> getData() {
        return data;
    }
}
