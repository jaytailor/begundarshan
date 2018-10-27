package com.jayt.begundarshan.model;

import com.jayt.begundarshan.interfaces.BaseModel;

import java.util.ArrayList;

public class WishMessageParentModel implements BaseModel {

    ArrayList<WishMessages> wishMessageList;

    public WishMessageParentModel() {
    }

    public WishMessageParentModel(ArrayList<WishMessages> wishMessageList) {
        this.wishMessageList = wishMessageList;
    }

    public ArrayList<WishMessages> getWishMessageList() {
        return wishMessageList;
    }

    public void setWishMessageList(ArrayList<WishMessages> wishMessageList) {
        this.wishMessageList = wishMessageList;
    }

    @Override
    public int getViewType() {
        return Constants.ViewType.WISH_TYPE;
    }
}
