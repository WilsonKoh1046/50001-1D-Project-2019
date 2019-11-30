package com.example.a1dproject_campustradingapp;

import com.google.firebase.database.Exclude;

public class Upload {
    private String mName;
    private String mImageUrl;
    private String mKey;
    private String mPrice;
    private String mDescription;
    private String mContactInfo;

    public Upload(){
        //empty constructor needed to work with firebase database
    }

    public Upload(String name, String imageUrl, String price, String contactInfo, String description){
        if(name.trim().equals("")){
            name = "No Name";
        }
        mName = name;
        mImageUrl = imageUrl;
        mDescription = description;
        mPrice = price;
        mContactInfo = contactInfo;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String name){
        mName = name;
    }

    public String getmImageUrl(){
        return mImageUrl;
    }

    public void setmImageUrl(String imageUrl){
        mImageUrl = imageUrl;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmContactInfo() {
        return mContactInfo;
    }

    public void setMstudentId(String contactInfo) {
        this.mContactInfo = contactInfo;
    }

    @Exclude
    public String getmKey(){
        return mKey;
    }

    @Exclude
    public void setmKey(String key){
        mKey = key;
    }
}


