package com.jayt.begundarshan.model;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.jayt.begundarshan.model.Constants.ViewType.BREAKING_NEWS_TYPE;
import static com.jayt.begundarshan.model.Constants.ViewType.NEWS_TYPE;
import static com.jayt.begundarshan.model.Constants.ViewType.AD_TYPE;
import static com.jayt.begundarshan.model.Constants.ViewType.EDITORIAL_TYPE;
import static com.jayt.begundarshan.model.Constants.ViewType.VIDEO_TYPE;
import static com.jayt.begundarshan.model.Constants.ViewType.WISH_TYPE;
import static com.jayt.begundarshan.model.Constants.ViewType.SURVEY_TYPE;

public class Constants {

    @IntDef({NEWS_TYPE, AD_TYPE, EDITORIAL_TYPE, WISH_TYPE, SURVEY_TYPE, VIDEO_TYPE, BREAKING_NEWS_TYPE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewType {
        int NEWS_TYPE = 100;
        int AD_TYPE = 200;
        int EDITORIAL_TYPE = 300;
        int WISH_TYPE = 400;
        int SURVEY_TYPE = 500;
        int VIDEO_TYPE = 600;
        int BREAKING_NEWS_TYPE = 700;
    }
}
