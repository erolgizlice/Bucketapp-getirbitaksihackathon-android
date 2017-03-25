package com.getirbitaksihackathon.erolgizlice.bucketapp_getirbitaksihackathon_android;

/**
 * Created by erolg on 25.03.2017.
 */

public class Event {
    String description, name, start_time,id;

    public Event(String description, String name, String start_time, String id) {
        this.description = description;
        this.name = name;
        this.start_time = start_time;
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Event{" +
                "description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", start_time='" + start_time + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
