package com.sociit.app.sociit.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.model.Marker;
import com.sociit.app.sociit.MyApplication;
import com.sociit.app.sociit.R;
import com.sociit.app.sociit.entities.Activity;
import com.sociit.app.sociit.entities.Building;
import com.sociit.app.sociit.entities.Session;
import com.sociit.app.sociit.entities.User;
import com.sociit.app.sociit.fragments.AboutFragment;
import com.sociit.app.sociit.fragments.ActivityDetailsFragment;
import com.sociit.app.sociit.fragments.ActivityFragment;
import com.sociit.app.sociit.fragments.AddActivityFragment;
import com.sociit.app.sociit.fragments.BuildingFragment;
import com.sociit.app.sociit.fragments.HomeFragment;
import com.sociit.app.sociit.fragments.NewsFragment;
import com.sociit.app.sociit.fragments.SettingsFragment;
import com.sociit.app.sociit.gcm.GCMCommonUtils;
import com.sociit.app.sociit.gcm.MyGCMRegistrationIntentService;
import com.sociit.app.sociit.helpers.ConstantValues;
import com.sociit.app.sociit.helpers.NotificationSendHelper;
import com.sociit.app.sociit.helpers.SqlHelper;
import com.sociit.app.sociit.helpers.StringUtil;
import com.sociit.app.sociit.helpers.TwitterHelper;

import java.util.Date;
import java.util.Stack;

import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;

public class MainActivity extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        AboutFragment.OnFragmentInteractionListener,
        ActivityFragment.OnListFragmentInteractionListener,
        BuildingFragment.OnListFragmentInteractionListener,
        NewsFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener,
        ActivityDetailsFragment.OnFragmentInteractionListener,
        AddActivityFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener {
    private SqlHelper db;
    private boolean dialog_open = false;
    private User user;
    String title;
    private Stack<String> titleHistory = new Stack<>();
    public static FragmentManager fragmentManager;
    private static Session session;

    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startRegistrationService(true, true);

        setContentView(R.layout.activity_main);
        title = getString(R.string.app_name);
        titleHistory.push(title);
        db = new SqlHelper(getApplicationContext());
        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        mTracker = ((MyApplication) getApplication()).getDefaultTracker();
        mTracker.setScreenName("Main");
        mTracker.send(new HitBuilders.ScreenViewBuilder()
                .build());
        // [END shared_tracker]
        try {
            session = ((MyApplication) getApplication()).getSession();
            user = db.getUserByUsername(session.getUser().getUsername());
            welcomeToast(user);
        } catch (Exception e) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        fragmentManager = getSupportFragmentManager();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog_open) {
                    closeDialog();
                    dialog_open = false;
                } else {
                    openAddActivityFragment();
                    dialog_open = true;
                }
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        menuInitialState(navigationView);

    }

    public void welcomeToast(User user) {
        Context context = getApplicationContext();
        CharSequence text = "Welcome " + user.getName() + "!";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void menuInitialState(NavigationView navigationView) {
        navigationView.getMenu().getItem(0).setChecked(true);
        Fragment fragment = new HomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }

    public void openAddActivityFragment() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        ViewCompat.animate(fab)
                .rotation(45)
                .setDuration(500)
                .setInterpolator(new BounceInterpolator())
                .start();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        AddActivityFragment addActivityFragment = new AddActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userName", this.user.getName());
        addActivityFragment.setArguments(bundle);
        titleHistory.push(title);
        ft.replace(R.id.content_frame, addActivityFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void closeDialog() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        ViewCompat.animate(fab)
                .rotation(0)
                .setDuration(500)
                .setInterpolator(new BounceInterpolator())
                .start();
        getSupportFragmentManager().popBackStackImmediate();
    }

    ;

    @Override
    public void onBackPressed() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            ViewCompat.animate(fab)
                    .rotation(0)
                    .setDuration(500)
                    .setInterpolator(new BounceInterpolator())
                    .start();
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(titleHistory.pop());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SettingsFragment fragment = new SettingsFragment();
            title = getResources().getString(R.string.action_settings);
            // set the toolbar title
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(title);
            }
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.addToBackStack(null);
            ft.commit();
            return true;
        } else if (id == R.id.action_sign_out) {
            ((MyApplication) getApplication()).setSession(null);
            startRegistrationService(false, false);
            // Redirect to MainActivity
            TwitterHelper.getInstance().logoutTwitter(getApplicationContext());
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        closeDialog();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.show();
        // Handle navigation view item clicks here.
        displayView(item.getItemId());
        return true;
    }

    public void displayView(int viewId) {
        dialog_open = false;
        Fragment fragment = null;
        switch (viewId) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                title = getResources().getString(R.string.home);
                // [START custom_event]
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Home")
                        .setAction("Action")
                        .build());
                mTracker.setScreenName("Home");
                mTracker.send(new HitBuilders.ScreenViewBuilder()
                        .build());
                // [END custom_event]
                break;
            case R.id.nav_my_activities:
                fragment = new ActivityFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("userId", this.user.getId());
                fragment.setArguments(bundle);
                title = getResources().getString(R.string.my_activities);
                // [START custom_event]
                mTracker.setScreenName("My activities");
                mTracker.send(new HitBuilders.ScreenViewBuilder()
                        .build());
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("My Activities")
                        .setAction("Action")
                        .build());
                // [END custom_event]
                break;
            case R.id.nav_buildings:
                fragment = new BuildingFragment();
                title = getResources().getString(R.string.buildings);
                // [START custom_event]
                mTracker.setScreenName("Buildings");
                mTracker.send(new HitBuilders.ScreenViewBuilder()
                        .build());
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Buildings")
                        .setAction("Action")
                        .build());
                // [END custom_event]
                break;
            case R.id.nav_activities:
                fragment = new ActivityFragment();
                title = getResources().getString(R.string.activities);
                // [START custom_event]
                mTracker.setScreenName("Activities");
                mTracker.send(new HitBuilders.ScreenViewBuilder()
                        .build());
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Activities")
                        .setAction("Action")
                        .build());
                // [END custom_event]
                break;
            case R.id.nav_news:
                fragment = new NewsFragment();
                title = getResources().getString(R.string.news);
                // [START custom_event]
                mTracker.setScreenName("News");
                mTracker.send(new HitBuilders.ScreenViewBuilder()
                        .build());
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("News")
                        .setAction("Action")
                        .build());
                // [END custom_event]
                break;
            case R.id.nav_about:
                fragment = new AboutFragment();
                title = getResources().getString(R.string.about);
                // [START custom_event]
                mTracker.setScreenName("About");
                mTracker.send(new HitBuilders.ScreenViewBuilder()
                        .build());
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("About")
                        .setAction("Action")
                        .build());
                // [END custom_event]
                break;
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void onFragmentInteraction(int userId) {
        dialog_open = false;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        ViewCompat.animate(fab)
                .rotation(0)
                .setDuration(500)
                .setInterpolator(new BounceInterpolator())
                .start();
        Fragment fragment = new ActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("userId", user.getId());
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Marker marker) {
        Building building = db.getBuildingByName(marker.getTitle());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(building.getName());
        }
        Fragment fragment = new ActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("buildingId", building.getId());
        fragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        titleHistory.push(title);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onListFragmentInteraction(Activity activity) {
        Fragment fragment = new ActivityDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("activityId", activity.getId());
        fragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        titleHistory.push(title);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onListFragmentInteraction(Building building) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(building.getName());
        }
        Fragment fragment = new ActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("buildingId", building.getId());
        fragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        titleHistory.push(title);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void twit(String twit) {
        Date date = new Date();
        String[] dateArray = date.toString().split(" ");
        String day = dateArray[2];
        String month = dateArray[1];
        String year = dateArray[5];
        String time = dateArray[3];

        twit = "[" + month + "/" + day + "/" + year + " " + time + "] - " + twit + " in SocIIT: www.iit.edu #sociit";

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sharedPreferences.getBoolean(ConstantValues.PREFERENCE_TWITTER_IS_LOGGED_IN, true)) {
            new TwitterUpdateStatusTask().execute(twit);
        }
    }

    public void notification(String message) {
        new SendNotificationTask().execute(message);
    }


    class SendNotificationTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            NotificationSendHelper nsh = new NotificationSendHelper();
            nsh.sendNotification(params[0]);
            return true;
        }
    }

    class TwitterUpdateStatusTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPostExecute(Boolean result) {
            if (result)
                Toast.makeText(getApplicationContext(), "Tweet successfully", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String accessTokenString = sharedPreferences.getString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN, "");
                String accessTokenSecret = sharedPreferences.getString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, "");

                if (!StringUtil.isNullOrWhitespace(accessTokenString) && !StringUtil.isNullOrWhitespace(accessTokenSecret)) {
                    AccessToken accessToken = new AccessToken(accessTokenString, accessTokenSecret);
                    twitter4j.Status status = TwitterHelper.getInstance().getTwitterFactory().getInstance(accessToken).updateStatus(params[0]);
                    return true;
                }

            } catch (TwitterException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return false;  //To change body of implemented methods use File | Settings | File Templates.

        }
    }


    public void startRegistrationService(boolean reg, boolean tkr) {
        if (GCMCommonUtils.checkPlayServices(this)) {
            Toast.makeText(this, "Registering for GCM...", Toast.LENGTH_LONG).show();
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, MyGCMRegistrationIntentService.class);
            intent.putExtra("register", reg);
            intent.putExtra("tokenRefreshed", tkr);
            startService(intent);
        }
    }


}
