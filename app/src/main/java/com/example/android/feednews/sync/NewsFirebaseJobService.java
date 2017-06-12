package com.example.android.feednews.sync;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
/**
 * Created by nikhil on 12/6/17.
 */

public class NewsFirebaseJobService extends JobService{
    @Override
    public boolean onStartJob(JobParameters job) {
        Log.v("NewsFirebaseJobService:","onStartJob() called.");
        NewsTasks.getData(NewsFirebaseJobService.this);
        jobFinished(job,false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.v("NewsFirebaseJobService:","onStopJob() called.");
        return true;
    }
}
