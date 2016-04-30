package com.sociit.app.sociit.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 */
public final class TwitterHelper {

    private RequestToken requestToken = null;
    private TwitterFactory twitterFactory = null;
    private Twitter twitter;

    private TwitterHelper() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(ConstantValues.TWITTER_CONSUMER_KEY);
        configurationBuilder.setOAuthConsumerSecret(ConstantValues.TWITTER_CONSUMER_SECRET);
        Configuration configuration = configurationBuilder.build();
        twitterFactory = new TwitterFactory(configuration);
        twitter = twitterFactory.getInstance();
    }

    public TwitterFactory getTwitterFactory()
    {
        return twitterFactory;
    }

    public void setTwitterFactory(AccessToken accessToken)
    {
        twitter = twitterFactory.getInstance(accessToken);
    }

    public Twitter getTwitter()
    {
        return twitter;
    }
    public RequestToken getRequestToken() {
        if (requestToken == null) {
            try {
                requestToken = twitterFactory.getInstance().getOAuthRequestToken(ConstantValues.TWITTER_CALLBACK_URL);
            } catch (TwitterException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return requestToken;
    }

    static TwitterHelper instance = new TwitterHelper();

    public static TwitterHelper getInstance() {
        return instance;
    }


    public void reset() {
        instance = new TwitterHelper();
    }


    public void logoutTwitter(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN, "");
        editor.putString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, "");
        editor.putBoolean(ConstantValues.PREFERENCE_TWITTER_IS_LOGGED_IN, false);
        editor.commit();
        TwitterHelper.getInstance().reset();

    }
}
