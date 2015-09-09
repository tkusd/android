package tw.tkusd.diff.model;

import java.util.UUID;

/**
 * Created by SkyArrow on 2015/9/9.
 */
public class Project extends BaseModel {
    private String title;
    private String description;
    private UUID userId;
    private boolean isPrivate;
    private UUID mainScreen;
    private String theme;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public UUID getMainScreen() {
        return mainScreen;
    }

    public void setMainScreen(UUID mainScreen) {
        this.mainScreen = mainScreen;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
