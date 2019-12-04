package com.example.a1dproject_campustradingapp;

import com.google.firebase.database.Exclude;

public class Upload {
    private String mName;
    private String mImageUrl;
    private String mKey;
    private String mPrice;
    private String mDescription;
    private String mContactInfo;
    private String mCategory;
    private String mImageUri;

    public Upload(){
        //empty constructor needed to work with firebase database
    }

    public Upload(String uri, String category,String name, String imageUrl, String price, String contactInfo, String description){
        if(name.trim().equals("")){
            name = "No Name";
        }
        mImageUri = uri;
        mCategory = category;
        mName = name;
        mImageUrl = imageUrl;
        mDescription = description;
        mPrice = price;
        mContactInfo = contactInfo;
    }

    public void setmContactInfo(String mContactInfo) {
        this.mContactInfo = mContactInfo;
    }

    public String getmImageUri() {
        return mImageUri;
    }

    public void setmImageUri(String mImageUri) {
        this.mImageUri = mImageUri;
    }

    public String getmCategory(){
        return mCategory;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
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


