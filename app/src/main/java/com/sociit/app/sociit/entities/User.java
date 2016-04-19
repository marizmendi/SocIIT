package com.sociit.app.sociit.entities;

import java.util.List;

/**
 * Created by Manuel on 19/04/2016.
 */
public class User {
    int id;
    String name;
    String username;
    String password;
    List<Activity> activityList;

    public User(int id, String username, String name, String password, List<Activity> activityList) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.password = password;
        this.activityList = activityList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
