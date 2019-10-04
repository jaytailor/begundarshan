package com.jayt.begundarshan.model;

import com.jayt.begundarshan.interfaces.BaseModel;

public class BreakingNews implements BaseModel {

    public BreakingNews() {
    }

    String id;
    String message;
    String published_at;
    String pushed_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPublished_at() {
        return published_at;
    }

    public void setPublished_at(String published_at) {
        this.published_at = published_at;
    }

    public String getPushed_at() {
        return pushed_at;
    }

    public void setPushed_at(String pushed_at) {
        this.pushed_at = pushed_at;
    }

    @Override
    public int getViewType() {
        return Constants.ViewType.BREAKING_NEWS_TYPE;
    }
}
