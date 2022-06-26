package net.manish.mybscitsem06;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("ALL")
public class Item
{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("url")
    @Expose
    private final String url  = null;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("content")
    @Expose
    private String content;

    /*public String getId()
    {
        return id;
    }*/

    /*public void setId(String id)
    {
        this.id = id;
    }*/

    public String getUrl()
    {
        return url;
    }


    public String getTitle()
    {
        return title;
    }

    /*public void setTitle(String title)
    {
        this.title = title;
    }*/

    public String getContent()
    {
        return content;
    }

    /*public void setContent(String content)
    {
        this.content = content;
    }*/


}
