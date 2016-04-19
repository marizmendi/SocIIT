package com.sociit.app.sociit.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.util.Log;

import com.sociit.app.sociit.entities.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;


/**
 * Created by Manuel on 19/04/2016.
 */
public class SqlHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "SqlHelper";

    // Database Version
    private static final int DATABASE_VERSION = 3;
    // Database Name
    private static final String DATABASE_NAME = "SociitDB";

    // Table Names
    private static final String TABLE_ACTIVITY = "activities";
    private static final String TABLE_BUILDING = "buildings";
    private static final String TABLE_COMMENT = "comments";
    private static final String TABLE_USER = "users";
    private static final String TABLE_USER_ACTIVITY = "users_activities";


    // Common column names
    private static final String KEY_ID = "id";

    // ACTIVITIES Table - column names
    private static final String KEY_ACTIVITY_NAME = "name";
    private static final String KEY_ACTIVITY_BUILDING = "building";
    private static final String KEY_ACTIVITY_USER = "user";

    // BUILDINGS Table - column names
    private static final String KEY_BUILDING_NAME = "name";
    private static final String KEY_BUILDING_ADDRESS = "address";

    // COMMENTS Table - column names
    private static final String KEY_COMMENT_MESSAGE = "message";
    private static final String KEY_COMMENT_USER = "user";
    private static final String KEY_COMMENT_ACTIVITY = "activity";

    // USERS Table - column names
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_USER_USERNAME = "username";
    private static final String KEY_USER_PASSWORD = "password";

    // USERS_ACTIVITIES Table - column names
    private static final String KEY_USER_ACTIVITY_USER = "user";
    private static final String KEY_USER_ACTIVITY_ACTIVITY = "activity";

    // Table Create Statements
    // Activity table create statement
    private static final String CREATE_TABLE_ACTIVITY =
            "CREATE TABLE " + TABLE_ACTIVITY + "(" +
                    KEY_ID + " INTEGER PRIMARY KEY," +
                    KEY_ACTIVITY_NAME + " TEXT," +
                    KEY_ACTIVITY_BUILDING + " INTEGER, " +
                    "FOREIGN KEY(" + KEY_ACTIVITY_BUILDING + ") REFERENCES buildings(" + KEY_ID + ")" +
                    ")";

    // Building table create statement
    private static final String CREATE_TABLE_BUILDING =
            "CREATE TABLE " + TABLE_BUILDING + "(" +
                    KEY_ID + " INTEGER PRIMARY KEY," +
                    KEY_BUILDING_NAME + " TEXT," +
                    KEY_BUILDING_ADDRESS + " TEXT " +
                    ")";

    // Comment table create statement
    private static final String CREATE_TABLE_COMMENT =
            "CREATE TABLE " + TABLE_COMMENT + "(" +
                    KEY_ID + " INTEGER PRIMARY KEY," +
                    KEY_COMMENT_MESSAGE + " TEXT," +
                    KEY_COMMENT_USER + " INTEGER," +
                    KEY_COMMENT_ACTIVITY + " INTEGER, " +
                    "FOREIGN KEY(" + KEY_COMMENT_USER + ") REFERENCES users(" + KEY_ID + "), " +
                    "FOREIGN KEY(" + KEY_COMMENT_ACTIVITY + ") REFERENCES activities(" + KEY_ID + ") " +
                    ")";

    // User table create statement
    private static final String CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE_USER + "(" +
                    KEY_ID + " INTEGER PRIMARY KEY," +
                    KEY_USER_NAME + " TEXT," +
                    KEY_USER_USERNAME + " TEXT UNIQUE," +
                    KEY_USER_PASSWORD + " TEXT" +
                    ")";

    private static final String CREATE_TABLE_USER_ACTIVITY =
            "CREATE TABLE " + TABLE_USER_ACTIVITY + "(" +
                    KEY_USER_ACTIVITY_USER + " INTEGER," +
                    KEY_USER_ACTIVITY_ACTIVITY + " INTEGER, " +
                    "FOREIGN KEY(" + KEY_USER_ACTIVITY_USER + ") REFERENCES users(" + KEY_ID + "), " +
                    "FOREIGN KEY(" + KEY_USER_ACTIVITY_ACTIVITY + ") REFERENCES activities(" + KEY_ID + ") " +
                    ")";

    public SqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_ACTIVITY);
        db.execSQL(CREATE_TABLE_BUILDING);
        db.execSQL(CREATE_TABLE_COMMENT);
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_USER_ACTIVITY);

        prepopulateDB(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUILDING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_ACTIVITY);

        // create new tables
        onCreate(db);
    }

    private void prepopulateDB(SQLiteDatabase db) {
        Address building_address = new Address(Locale.getDefault());
        building_address.setAddressLine(0, "31 street");
        Building building = new Building(0, building_address, "Edificio1", null);
        Activity activity = new Activity(0, "Actividad1", building, null, null);
        this.addActivity(activity, db);

        User user1 = new User(0, "foo", "FOO", "hello", null);
        this.addUser(user1, db);
        User user2 = new User(0, "bar", "BAR", "world", null);
        this.addUser(user2, db);

    }

    // TODO: Editar las siguientes funciones para que hagan peticiones a la base de datos.

    public void addUser(User user) {

        SQLiteDatabase db = this.getWritableDatabase();
        addUser(user, db);
        db.close();
    }

    public void addUser(User user, SQLiteDatabase db) {

        Log.d("addUser", user.toString());

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, user.getName());
        values.put(KEY_USER_USERNAME, user.getUsername());
        values.put(KEY_USER_PASSWORD, user.getPassword());

        // 3. insert
        db.insert(TABLE_USER, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/values
    }

    public User getUserByUsername(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        User user = getUserByUsername(username, db);
        db.close();
        return user;
    }

    public User getUserByUsername(String username, SQLiteDatabase db) {
        String query = "SELECT * FROM " + TABLE_USER + " WHERE username LIKE \"" + username + "\"";
        List<User> users = new LinkedList<User>();
        Cursor cursor = db.rawQuery(query, null);

        User user = null;
        if (cursor.moveToFirst()) {
            do {
                user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setName(cursor.getString(1));
                user.setUsername(cursor.getString(2));
                user.setPassword(cursor.getString(3));

                users.add(user);
            } while (cursor.moveToNext());
        }

        User returnUser;

        if(users.size()==0){
            returnUser = null;
        } else {
            returnUser = users.get(0);
        }

        return returnUser;
    }

    public void editUser(User user) {
        return;
    }

    public void deleteUser(User user) {
        return;
    }

    public void addActivity(Activity activity) {
        SQLiteDatabase db = this.getWritableDatabase();
        addActivity(activity, db);
        db.close();
    }

    public void addActivity(Activity activity, SQLiteDatabase db) {

        Log.d("addActivity", activity.toString());

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_ACTIVITY_NAME, activity.getName()); // get name
        values.put(KEY_ACTIVITY_BUILDING, activity.getBuilding().getId()); // get building
        //values.put(KEY_ACTIVITY_USER, activity.getUserList().get(0).getId());

        // 3. insert
        db.insert(TABLE_ACTIVITY, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/values
    }

    public Activity getActivityById(int id) {
        return null;
    }

    public List<Activity> getAllActivities() {
        List<Activity> activities = new LinkedList<Activity>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_ACTIVITY;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Activity activity = null;
        if (cursor.moveToFirst()) {
            do {
                activity = new Activity();
                activity.setId(Integer.parseInt(cursor.getString(0)));
                activity.setName(cursor.getString(1));

                // Add book to books
                activities.add(activity);
            } while (cursor.moveToNext());
        }

        Log.d("getAllActivities()", activities.toString());
        db.close();

        return activities; // return books
    }

    public List<Activity> getUserActivities(User user) {
        return null;
    }

    public List<Activity> getBuildingActivities(Building building) {
        return null;
    }

    public void editActivity(Activity activity) {
        return;
    }

    public void deleteActivity(Activity activity) {
        return;
    }

    public List<Building> getAllBuildings() {
        return null;
    }

    public void addComment(Comment comment) {
        return;
    }

    public List<Comment> getActivityComments(Activity activity) {
        return null;
    }

}
