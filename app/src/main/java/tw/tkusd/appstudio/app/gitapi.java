package tw.tkusd.appstudio.app;

import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by wenlin on 2015/7/23.
 */
public interface gitapi{

    @POST("/v1/users")
    void login(@Body User user, Callback<Result> callback);

}
