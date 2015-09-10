package tw.tkusd.diff.event;

import retrofit.Response;
import tw.tkusd.diff.model.Token;
import tw.tkusd.diff.model.TokenRequest;

/**
 * Created by SkyArrow on 2015/9/10.
 */
public class LoginEvent {
    private TokenRequest request;
    private Response<Token> response;
    private Throwable error;

    public LoginEvent(TokenRequest request, Response<Token> response) {
        this.request = request;
        this.response = response;
    }

    public LoginEvent(Throwable error) {
        this.error = error;
    }

    public TokenRequest getRequest() {
        return request;
    }

    public void setRequest(TokenRequest request) {
        this.request = request;
    }

    public Response<Token> getResponse() {
        return response;
    }

    public void setResponse(Response<Token> response) {
        this.response = response;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}
