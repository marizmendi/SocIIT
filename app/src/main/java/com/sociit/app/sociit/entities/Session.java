package com.sociit.app.sociit.entities;

import com.sociit.app.sociit.entities.User;
import com.sociit.app.sociit.helpers.TwitterHelper;

/**
 * Created by Manuel on 22/04/2016.
 */
public class Session {
    private User user;

    public Session() {

    }

    public Session(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
