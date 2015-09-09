package tw.tkusd.diff.model;

import java.util.List;

/**
 * Created by SkyArrow on 2015/9/10.
 */
public class ProjectList {
    private List<Project> data;
    private boolean hasMore;
    private int count;
    private int limit;
    private int offset;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Project> getData() {
        return data;
    }

    public void setData(List<Project> data) {
        this.data = data;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
