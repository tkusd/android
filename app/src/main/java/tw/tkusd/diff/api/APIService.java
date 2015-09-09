package tw.tkusd.diff.api;

import java.util.UUID;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import tw.tkusd.diff.model.Project;
import tw.tkusd.diff.model.ProjectList;
import tw.tkusd.diff.model.Token;
import tw.tkusd.diff.model.TokenRequest;
import tw.tkusd.diff.model.User;

/**
 * Created by SkyArrow on 2015/9/9.
 */
public interface APIService {
    // Users
    @POST("users")
    Call<User> createUser(@Body User body);

    @GET("users/{id}")
    Call<User> getUser(@Path("id") UUID userId);

    @PUT("users/{id}")
    Call<User> updateUser(@Path("id") UUID userId, @Body User body);

    @DELETE("users/{id}")
    Call deleteUser(@Path("id") UUID userId);

    // Tokens
    @POST("tokens")
    Call<Token> createToken(@Body TokenRequest req);

    @DELETE("tokens/{id}")
    Call<Void> deleteToken(@Path("id") UUID tokenId);

    // Projects
    @GET("users/{id}/projects")
    Call<ProjectList> getProjectList(@Path("id") UUID userId);

    @POST("users/{id}/projects")
    Call<Project> createProject(@Path("id") UUID userId, @Body Project body);

    @GET("projects/{id}")
    Call<Project> getProject(@Path("id") UUID projectId);
}
