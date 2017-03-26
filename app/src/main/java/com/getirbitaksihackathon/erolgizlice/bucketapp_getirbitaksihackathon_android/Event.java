package com.getirbitaksihackathon.erolgizlice.bucketapp_getirbitaksihackathon_android;

/**
 * Created by erolg on 25.03.2017.
 */

public class Event {
    String description, name, start_time, facebook_event_id;
    User user;
    Place place;

    public Event(String description, String name, String start_time, String facebook_event_id, Place place) {
        this.description = description;
        this.name = name;
        this.start_time = start_time;
        this.facebook_event_id = facebook_event_id;
        this.place = place;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getFacebook_event_id() {
        return facebook_event_id;
    }

    public void setFacebook_event_id(String facebook_event_id) {
        this.facebook_event_id = facebook_event_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    @Override
    public String toString() {
        return "Event{" +
                "description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", start_time='" + start_time + '\'' +
                ", facebook_event_id='" + facebook_event_id + '\'' +
                ", user=" + user +
                ", place=" + place +
                '}';
    }
}
