package com.example.android.feednews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private ListView news_list;
    private int count = 10;
    private NewsAdapter adapter;
    private String CULTURE = "culture";
    private String TECHNOLOGY = "technology";
    private String TRAVEL = "travel";
    private String WORLD = "world";
    private String SECTION = WORLD;
    private String URL = "https://content.guardianapis.com/search?&section=" + SECTION + "&page-size=" + count + "&show-fields=thumbnail,trailText&show-tags=contributor&api-key=6de1a8cd-9a9d-4016-8273-26de99416430";
    private CardView newsCard;
    private CardView readmore;
    private ConnectivityManager connected;
    NetworkInfo activeNetwork;
    public static final String LOG_TAG = NewsLoader.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        news_list = (ListView) findViewById(R.id.list_view);
        readmore = (CardView) findViewById(R.id.readmore);
        readmore.setVisibility(View.VISIBLE);
        adapter = new NewsAdapter(MainActivity.this, new ArrayList<News>());
        news_list.setAdapter(adapter);
        connected = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = connected.getActiveNetworkInfo();
        adapter.clear();
        newsCard = (CardView) findViewById(R.id.news_card);

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(0, null, this);

        news_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.setElevation(2);
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.setElevation(20);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        news_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News current_news = adapter.getItem(position);
                Uri bookUri = Uri.parse(current_news.getUrl());

                Intent news_intent = new Intent(Intent.ACTION_VIEW, bookUri);
                startActivity(news_intent);
            }
        });

        readmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count += 10;
                if (count < 41) {
                    URL = "https://content.guardianapis.com/search?&section=world&page-size=" + count + "&show-fields=thumbnail,trailText&show-tags=contributor&api-key=6de1a8cd-9a9d-4016-8273-26de99416430";
                    adapter.clear();
                    Log.i(LOG_TAG, URL);
                    getLoaderManager().restartLoader(0, null, MainActivity.this);
                    Log.d(LOG_TAG, "readmore clicked");
                } else {
                    readmore.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        Log.e(LOG_TAG, "onCreateLoader...");
        return new NewsLoader(this, URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        Log.e(LOG_TAG, "onLoaderFinished...");
        adapter.clear();
        if (news != null && !news.isEmpty()) {
            adapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        Log.e(LOG_TAG, "onLoaderReset...");
        adapter.clear();
    }
}
