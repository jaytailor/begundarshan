package com.jayt.begundarshan.model;

import com.jayt.begundarshan.interfaces.BaseModel;

public class SurveyModel implements BaseModel {

    String id;
    String surveyTitle;
    int yes;
    int no;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSurveyTitle() {
        return surveyTitle;
    }

    public void setSurveyTitle(String surveyTitle) {
        this.surveyTitle = surveyTitle;
    }

    public int getYes() {
        return yes;
    }

    public void setYes(int yes) {
        this.yes = yes;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    @Override
    public int getViewType() {
        return Constants.ViewType.SURVEY_TYPE;
    }
}
