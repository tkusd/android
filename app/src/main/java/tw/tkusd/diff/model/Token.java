package tw.tkusd.diff.model;

import java.util.UUID;

/**
 * Created by SkyArrow on 2015/9/9.
 */
public class Token extends BaseModel {
    private UUID userId;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
