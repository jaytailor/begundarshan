package com.jayt.begundarshan.model;

import com.jayt.begundarshan.interfaces.BaseModel;

public class NewsItems implements BaseModel {

    String id;
    String writer;
    String title;
    String content;
    String image;
    String is_breaking;
    String published_at;
    String pushed_at;

    public NewsItems() {
    }

    public NewsItems(String id, String writer, String title, String content, String image, String is_breaking, String published_at) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.image = image;
        this.is_breaking = is_breaking;
        this.published_at = published_at;
    }

    public String getId() {
        return id;
    }

    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImage() {
        return image;
    }

    public String getIs_breaking() {
        return is_breaking;
    }

    public String getPublished_at() {
        return published_at;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setIs_breaking(String is_breaking) {
        this.is_breaking = is_breaking;
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
        return Constants.ViewType.NEWS_TYPE;
    }
}

