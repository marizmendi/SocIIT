package com.sociit.app.sociit;

import android.app.Application;

import com.sociit.app.sociit.activities.Session;

/**
 * Created by Manuel on 22/04/2016.
 */
public class MyApplication extends Application {

    private Session session;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
