package com.getirbitaksihackathon.erolgizlice.bucketapp_getirbitaksihackathon_android;

/**
 * Created by erolg on 26.03.2017.
 */

public class User {

    String user_id;
    String name;
    String link;
    String gender;
    String age;
    String profilePicLink;
    String email;
    String locationID;
    Location location;

    public User(String user_id, String name, String link, String gender, String age, String profilePicLink, String email, String locationID, Location location) {
        this.user_id = user_id;
        this.name = name;
        this.link = link;
        this.gender = gender;
        this.age = age;
        this.profilePicLink = profilePicLink;
        this.email = email;
        this.locationID = locationID;
        this.location = location;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getProfilePicLink() {
        return profilePicLink;
    }

    public void setProfilePicLink(String profilePicLink) {
        this.profilePicLink = profilePicLink;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "{" +
                "\"facebook_user_id\":\"" + user_id + "\"" +
                ", \"name\":\"" + name + "\"" +
                ", \"link\":\"" + link + "\"" +
                ", \"gender\":\"" + gender + "\"" +
                ", \"age_range_max\":\"" + age + "\"" +
                ", \"profile_picture_uri\":\"" + profilePicLink + "\"" +
                ", \"email\":\"" + email + "\"" +
                "," + location.toString();
    }
}
