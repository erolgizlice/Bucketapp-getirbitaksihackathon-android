package com.getirbitaksihackathon.erolgizlice.bucketapp_getirbitaksihackathon_android;

/**
 * Created by erolg on 26.03.2017.
 */

public class Place {
    String name, id;
    Location location;

    public Place(String name, String id, Location location) {
        this.name = name;
        this.id = id;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Place{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", location=" + location +
                '}';
    }
}
