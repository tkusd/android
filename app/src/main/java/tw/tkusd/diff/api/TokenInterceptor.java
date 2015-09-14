package tw.tkusd.diff.api;

import android.content.Context;
import android.text.TextUtils;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.UUID;

import tw.tkusd.diff.util.TokenHelper;

/**
 * Created by SkyArrow on 2015/9/9.
 */
public class TokenInterceptor implements Interceptor {
    private final TokenHelper tokenHelper;

    public TokenInterceptor(Context context) {
        this.tokenHelper = TokenHelper.getInstance(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String secret = tokenHelper.getSecret();

        if (!TextUtils.isEmpty(secret)) {
            request = request.newBuilder()
                    .addHeader("Authorization", "Bearer " + secret)
                    .build();
        }

        return chain.proceed(request);
    }
}
