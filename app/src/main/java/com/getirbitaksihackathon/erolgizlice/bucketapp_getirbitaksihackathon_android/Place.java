package com.getirbitaksihackathon.erolgizlice.bucketapp_getirbitaksihackathon_android;

/**
 * Created by erolg on 26.03.2017.
 */

public class Place {
    String name, facebook_place_id;
    PlaceLocation placeLocation;

    public Place(String name, String facebook_place_id, PlaceLocation placeLocation) {
        this.name = name;
        this.facebook_place_id = facebook_place_id;
        this.placeLocation = placeLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return facebook_place_id;
    }

    public void setId(String id) {
        this.facebook_place_id = id;
    }

    public PlaceLocation getPlaceLocation() {
        return placeLocation;
    }

    public void setPlaceLocation(PlaceLocation placeLocation) {
        this.placeLocation = placeLocation;
    }

    @Override
    public String toString() {
        return "Place{" +
                "name='" + name + '\'' +
                ", facebook_place_id='" + facebook_place_id + '\'' +
                ", placeLocation=" + placeLocation +
                '}';
    }
}
