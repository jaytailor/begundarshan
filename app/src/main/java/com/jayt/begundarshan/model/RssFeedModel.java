package com.jayt.begundarshan.model;

import com.jayt.begundarshan.interfaces.BaseModel;

import java.util.ArrayList;

public class RssFeedModel implements BaseModel {
    String id;
    String rss_title;
    String rss_content;
    ArrayList<String> rss_image;
    String rss_published_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRss_title() {
        return rss_title;
    }

    public void setRss_title(String rss_title) {
        this.rss_title = rss_title;
    }

    public String getRss_content() {
        return rss_content;
    }

    public void setRss_content(String rss_content) {
        this.rss_content = rss_content;
    }

    public ArrayList<String> getRss_image() {
        return rss_image;
    }

    public void setRss_image(ArrayList<String> rss_image) {
        this.rss_image = rss_image;
    }

    public String getRss_published_at() {
        return rss_published_at;
    }

    public void setRss_published_at(String rss_published_at) {
        this.rss_published_at = rss_published_at;
    }

    @Override
    public int getViewType() {
        return Constants.ViewType.RSS_TYPE;
    }
}

