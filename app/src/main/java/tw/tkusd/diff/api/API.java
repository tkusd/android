package tw.tkusd.diff.api;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import tw.tkusd.diff.Constant;
import tw.tkusd.diff.model.APIError;

/**
 * Created by SkyArrow on 2015/9/9.
 */
public class API {
    private static API instance;

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final FieldNamingPolicy FIELD_NAMING_POLICY = FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;
    private static final Gson gson = new GsonBuilder()
            .setDateFormat(DATE_FORMAT)
            .setFieldNamingPolicy(FIELD_NAMING_POLICY)
            .create();

    private final Context context;
    private final APIService service;

    private API(Context context) {
        this.context = context;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        retrofit.client().interceptors().add(new TokenInterceptor(context));

        this.service = retrofit.create(APIService.class);
    }

    public static API getInstance(Context context) {
        if (instance == null) {
            instance = new API(context.getApplicationContext());
        }

        return instance;
    }

    public APIService getService() {
        return service;
    }

    public Gson getGson() {
        return gson;
    }

    public static APIError parseAPIError(ResponseBody body) {
        if (body == null) return null;

        try {
            return gson.fromJson(body.string(), APIError.class);
        } catch (IOException e) {
            return null;
        }
    }
}
