package tw.tkusd.appstudio.app;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;


/**
 * Created by wenlin on 2015/7/23.
 */
public interface API {

    @POST("/v1/users")
    void signup(@Body User user,Callback<User> callback);
    @POST("/v1/tokens")
    void login(@Body Token token ,Callback<Token> callback);
    @DELETE("/v1/tokens/{tokenid}")
    void deleteToken(@Path("tokenid") String tokenid,Callback<User> callback);
    @DELETE("/v1/users/{userid}")
    void deleteAccount(@Path("userid") String userid,Callback<User> callback);
    @PUT("/v1/users/{userid}")
    void updateUser(@Body User user,@Path("userid") String userid,Callback<User> callback);
    @GET("/v1/users/{userid}")
    void getUser(@Path("userid") String userid,Callback<User> callback);
    @GET("/v1/users/{user_id}/projects")
    void projects (@Path("user_id") String userid, Callback<ProjectList> callback);

}
