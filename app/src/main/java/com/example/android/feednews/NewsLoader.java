package com.example.android.feednews;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by NIKHIL on 14-02-2017.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private String mUrl;
    private static final String LOG_TAG = NewsLoader.class.getName();

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        Log.e(LOG_TAG, "loadInBackground...");
        if (mUrl == null)
            return null;
        return QueryUtils.getNews(mUrl);
    }
}
