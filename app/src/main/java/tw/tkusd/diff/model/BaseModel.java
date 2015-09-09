package tw.tkusd.diff.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by SkyArrow on 2015/9/9.
 */
public abstract class BaseModel {
    private UUID id;
    private Date createdAt;
    private Date updatedAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public UUID getId() {
        return id;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
