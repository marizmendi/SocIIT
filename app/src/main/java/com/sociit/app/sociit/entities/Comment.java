package com.sociit.app.sociit.entities;

/**
 * Created by Manuel on 19/04/2016.
 */
public class Comment {
    int id;
    String message;
    User user;
    Activity activity;

    public Comment(int id, String message, User user, Activity activity) {
        this.id = id;
        this.message = message;
        this.user = user;
        this.activity = activity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
