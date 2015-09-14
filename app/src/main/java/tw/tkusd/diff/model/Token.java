package tw.tkusd.diff.model;

import java.util.UUID;

/**
 * Created by SkyArrow on 2015/9/9.
 */
public class Token extends BaseModel {
    private UUID userId;
    private String secret;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
