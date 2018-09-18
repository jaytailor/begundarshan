package com.jayt.begundarshan.model;

public class AdsList {

    String id;
    String imageurl;
    String expired;
    String start_date;
    String end_date;
    String impression_limit;
    String impression_freq;
    String priority;
    String current_impression_count;

    public AdsList() {
    }

    public AdsList(String id, String imageurl, String expired, String start_date, String end_date,
                   String impression_limit, String impression_freq, String priority, String current_impression_count) {
        this.id = id;
        this.imageurl = imageurl;
        this.expired = expired;
        this.start_date = start_date;
        this.end_date = end_date;
        this.impression_limit = impression_limit;
        this.impression_freq = impression_freq;
        this.priority = priority;
        this.current_impression_count = current_impression_count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

}

