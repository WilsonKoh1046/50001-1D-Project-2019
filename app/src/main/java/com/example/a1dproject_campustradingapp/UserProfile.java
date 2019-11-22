package com.example.a1dproject_campustradingapp;

//Create all information we need to upload to database
public class UserProfile {
    private String Name;
    private String ID;
    private String Email;

    public UserProfile(){}

    public UserProfile(String userName, String userId, String userEmail) {
        this.Name = userName;
        this.ID = userId;
        this.Email = userEmail;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}