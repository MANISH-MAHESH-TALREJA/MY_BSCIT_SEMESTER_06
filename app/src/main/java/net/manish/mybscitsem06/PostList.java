
package net.manish.mybscitsem06;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

@SuppressWarnings("ALL")
public class PostList
{
    @SerializedName("nextPageToken")
    @Expose
    private final String nextPageToken  = null;
    @SerializedName("items")
    @Expose
    private final List<Item> items = null;
    public List<Item> getItems() {
        return items;
    }
    public String getNextPageToken() {
        return nextPageToken;
    }
}
