package com.example.android.feednews.sync;

import android.content.Context;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * Created by nikhil on 12/6/17.
 */

public class NewsUtilities {

    private static final int INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(1));
    private static final String JOB_TAG = "news-tag";

    private static boolean sInitialized;

    synchronized public static void scheduleNews(final Context context){
        if (sInitialized) return;
        Log.v("NewsUtilities:"," scheduleNews() called.");
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(driver);
        Job newsJob = firebaseJobDispatcher.newJobBuilder()
                .setService(NewsFirebaseJobService.class)
                .setTag(JOB_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(0,INTERVAL_SECONDS))
                .setReplaceCurrent(true).build();
        firebaseJobDispatcher.schedule(newsJob);
        sInitialized = true;
    }
}
