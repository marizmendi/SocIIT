package com.sociit.app.sociit;

import android.location.Address;

import java.util.List;

/**
 * Created by Manuel on 19/04/2016.
 */
public class Building {
    int id;
    String name;
    Address address;
    List<Activity> activityList;

    public Building(int id, Address address, String name, List<Activity> activityList) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.activityList = activityList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Activity> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<Activity> activityList) {
        this.activityList = activityList;
    }

    public void addActivity(Activity activity) {
        this.activityList.add(activity);
    }


}
