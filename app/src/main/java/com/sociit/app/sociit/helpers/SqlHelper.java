package com.sociit.app.sociit.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Manuel on 19/04/2016.
 */
public class SqlHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "SqlHelper";

    // Database Version
    private static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "SociitDB";

    // Table Names
    private static final String TABLE_ACTIVITY = "activities";
    private static final String TABLE_BUILDING = "buildings";
    private static final String TABLE_COMMENT = "comments";
    private static final String TABLE_USER = "users";

    // Common column names
    private static final String KEY_ID = "id";

    // ACTIVITIES Table - column names
    private static final String KEY_ACTIVITY_NAME = "name";
    private static final String KEY_ACTIVITY_BUILDING = "building";

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

    // Table Create Statements
    // Activity table create statement
    private static final String CREATE_TABLE_ACTIVITY =
            "CREATE TABLE " + TABLE_ACTIVITY + "(" +
                    KEY_ID + " INTEGER PRIMARY KEY," +
                    KEY_ACTIVITY_NAME + " TEXT," +
                    KEY_ACTIVITY_BUILDING + " INTEGER"
            + ")";

    // Building table create statement
    private static final String CREATE_TABLE_BUILDING =
            "CREATE TABLE " + TABLE_BUILDING + "(" +
                    KEY_ID + " INTEGER PRIMARY KEY," +
                    KEY_BUILDING_NAME + " TEXT," +
                    KEY_BUILDING_ADDRESS + " TEXT"
            + ")";

    // Comment table create statement
    private static final String CREATE_TABLE_COMMENT =
            "CREATE TABLE " + TABLE_COMMENT + "(" +
                    KEY_ID + " INTEGER PRIMARY KEY," +
                    KEY_COMMENT_MESSAGE + " TEXT," +
                    KEY_COMMENT_USER + " INTEGER," +
                    KEY_COMMENT_ACTIVITY + " INTEGER"
            + ")";

    // User table create statement
    private static final String CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE_USER + "(" +
                    KEY_ID + " INTEGER PRIMARY KEY," +
                    KEY_USER_NAME + " TEXT," +
                    KEY_USER_USERNAME + " TEXT," +
                    KEY_USER_PASSWORD + " TEXT"
            + ")";


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

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUILDING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // create new tables
        onCreate(db);
    }
}
