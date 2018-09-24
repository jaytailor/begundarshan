package com.jayt.begundarshan.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.jayt.begundarshan.model.Constants.ViewType.NEWS_TYPE;
import static com.jayt.begundarshan.model.Constants.ViewType.AD_TYPE;
import static com.jayt.begundarshan.model.Constants.ViewType.EDITORIAL_TYPE;

public class Constants {

    @IntDef({NEWS_TYPE, AD_TYPE, EDITORIAL_TYPE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewType {
        int NEWS_TYPE = 100;
        int AD_TYPE = 200;
        int EDITORIAL_TYPE = 300;
    }
}