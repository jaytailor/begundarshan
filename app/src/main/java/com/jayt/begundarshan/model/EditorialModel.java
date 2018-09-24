package com.jayt.begundarshan.model;

import com.jayt.begundarshan.interfaces.BaseModel;

public class EditorialModel implements BaseModel{
    String id;
    String editorial_title;
    String editorial_content;
    String editorial_image;
    String editorial_writer;
    String editorial_published_at;

    public EditorialModel() {
    }

    public EditorialModel(String id, String editorial_title, String editorial_content, String editorial_image, String editorial_writer, String editorial_published_at) {
        this.id = id;
        this.editorial_title = editorial_title;
        this.editorial_content = editorial_content;
        this.editorial_image = editorial_image;
        this.editorial_writer = editorial_writer;
        this.editorial_published_at = editorial_published_at;
    }

    public String getEditorial_title() {
        return editorial_title;
    }

    public void setEditorial_title(String editorial_title) {
        this.editorial_title = editorial_title;
    }

    public String getEditorial_content() {
        return editorial_content;
    }

    public void setEditorial_content(String editorial_content) {
        this.editorial_content = editorial_content;
    }

    public String getEditorial_image() {
        return editorial_image;
    }

    public void setEditorial_image(String editorial_image) {
        this.editorial_image = editorial_image;
    }

    public String getEditorial_writer() {
        return editorial_writer;
    }

    public void setEditorial_writer(String editorial_writer) {
        this.editorial_writer = editorial_writer;
    }

    public String getEditorial_published_at() {
        return editorial_published_at;
    }

    public void setEditorial_published_at(String editorial_published_at) {
        this.editorial_published_at = editorial_published_at;
    }

    @Override
    public int getViewType() {
        return Constants.ViewType.EDITORIAL_TYPE;
    }
}
