package tw.tkusd.appstudio.app;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;


/**
 * Created by wenlin on 2015/7/23.
 */
public interface API {

    @POST("/v1/users")
    void signup(@Body User user,Callback<User> callback);
    @POST("/v1/tokens")
    void token(@Body User user,Callback<User> callback);
    @DELETE("/v1/tokens/{tokenid}")
    void delete(@Path("tokenid") String tokenid,Callback<User> callback);
    @GET("/v1/users/{user_id}/projects")
    void projects (@Path("user_id") String userid, Callback<Project> callback);


}
