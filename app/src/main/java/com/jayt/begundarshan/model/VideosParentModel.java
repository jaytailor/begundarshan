package com.jayt.begundarshan.model;

import com.jayt.begundarshan.interfaces.BaseModel;

import java.util.ArrayList;

public class VideosParentModel implements BaseModel {
    ArrayList<YoutubeVideo> videoList;

    public VideosParentModel() {
    }

    public VideosParentModel(ArrayList<YoutubeVideo> videoList) {
        this.videoList = videoList;
    }

    public ArrayList<YoutubeVideo> getVideoList() {
        return videoList;
    }

    public void setVideoList(ArrayList<YoutubeVideo> videoList) {
        this.videoList = videoList;
    }

    @Override
    public int getViewType() {
        return Constants.ViewType.VIDEO_TYPE;
    }
}