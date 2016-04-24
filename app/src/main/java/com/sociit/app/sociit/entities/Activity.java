package com.sociit.app.sociit.entities;

import java.util.Date;
import java.util.List;

/**
 * Created by Manuel on 19/04/2016.
 */
public class Activity {
    int id;
    String name;
    Building building;
    Date date;
    List<User> userList;
    List<Comment> commentList;

    public Activity(int id, String name, Building building, Date date, List<User> userList, List<Comment> commentList) {
        this.id = id;
        this.name = name;
        this.userList = userList;
        this.building = building;

        this.commentList = commentList;
        this.date = date;
    }

    public Activity() {
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

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<User> getUserList() {
        return userList;
    }

    public User getCreator() {
        return this.userList != null ? this.userList.get(0) : null;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public void addUser(User user) {
        this.userList.add(user);
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public void addComment(Comment comment) {
        this.commentList.add(comment);
    }

    public int getNumberUsers (){
        return userList.size();
    }
}
