package com.example.a1dproject_campustradingapp;

import com.google.firebase.database.Exclude;

public class Favourites {
    private String fImageURL;
    private String fName;
    private String fPrice;
    private String fCategory;
    private String fContact;
    private String fDescription;
    private String fKey;

    public Favourites() {

    }

    public String getfImageURL() {
        return fImageURL;
    }

    public String getfName() {
        return fName;
    }

    public String getfPrice() {
        return fPrice;
    }

    public String getfCategory() {
        return fCategory;
    }

    public String getfContact() {
        return fContact;
    }

    public String getfDescription() {
        return fDescription;
    }

    public void setfImageURL(String fImageURL) {
        this.fImageURL = fImageURL;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setfPrice(String fPrice) {
        this.fPrice = fPrice;
    }

    public void setfCategory(String fCategory) {
        this.fCategory = fCategory;
    }

    public void setfContact(String fContact) {
        this.fContact = fContact;
    }

    public void setfDescription(String fDescription) {
        this.fDescription = fDescription;
    }


    @Exclude
    public String getfKey(){
        return fKey;
    }

    @Exclude
    public void setfKey(String key){
        fKey = key;
    }
}
