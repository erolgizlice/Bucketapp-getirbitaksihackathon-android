package com.getirbitaksihackathon.erolgizlice.bucketapp_getirbitaksihackathon_android;

/**
 * Created by erolg on 26.03.2017.
 */

public class PlaceLocation {
    String city;
    String country;
    String latitude;
    String longitude;

    public PlaceLocation(String city, String country, String latitude, String longitude) {
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longtitude) {
        this.longitude = longtitude;
    }

    @Override
    public String toString() {
        return "\"placeLocation\": {" +
                "\"city\":\"" + city + "\"" +
                ", \"country\":\"" + country + "\"" +
                ", \"latitude\":\"" + latitude + "\"" +
                ", \"longitude\":\"" + longitude + "\"" +
                "},";
    }
}
