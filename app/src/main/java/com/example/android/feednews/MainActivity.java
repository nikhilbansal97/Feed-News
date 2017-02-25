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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private ListView news_list;
    private int count = 10;
    private NewsAdapter adapter;
    private String CULTURE = "culture";
    private String TECHNOLOGY = "technology";
    private String TRAVEL = "travel";
    private String WORLD = "world";
    private String ENVIRONMENT = "environment";
    private String BUSINESS = "business";
    private String SPORT = "sport";
    private String SECTION = WORLD;
    private String URL = "https://content.guardianapis.com/search?&section=" + SECTION + "&page-size=" + count + "&show-fields=thumbnail,trailText&show-tags=contributor&api-key=6de1a8cd-9a9d-4016-8273-26de99416430";
    private CardView newsCard;
    private CardView readmore;
    private ProgressBar loading;
    private RelativeLayout no_internet;
    ConnectivityManager info;
    NetworkInfo activeNetwork;
    public static final String LOG_TAG = NewsLoader.class.getName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        no_internet = (RelativeLayout) findViewById(R.id.no_internet_view);
        news_list = (ListView) findViewById(R.id.list_view);
        readmore = (CardView) findViewById(R.id.readmore);
        readmore.setVisibility(View.VISIBLE);
        loading = (ProgressBar) findViewById(R.id.loading);
        adapter = new NewsAdapter(MainActivity.this, new ArrayList<News>());
        news_list.setAdapter(adapter);
        adapter.clear();
        news_list.setEmptyView(loading);
        newsCard = (CardView) findViewById(R.id.news_card);
        checkInternet();

        news_list.setOnItemClickListener(new AdapterView.OnItemClickListener()

                                         {
                                             @Override
                                             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                 News current_news = adapter.getItem(position);
                                                 Uri bookUri = Uri.parse(current_news.getUrl());

                                                 Intent news_intent = new Intent(Intent.ACTION_VIEW, bookUri);
                                                 startActivity(news_intent);
                                             }
                                         }

        );

        readmore.setOnClickListener(new View.OnClickListener()

                                    {
                                        @Override
                                        public void onClick(View v) {
                                            count += 10;
                                            if (count < 41) {
                                                URL = "https://content.guardianapis.com/search?&section=" + SECTION + "&page-size=" + count + "&show-fields=thumbnail,trailText&show-tags=contributor&api-key=6de1a8cd-9a9d-4016-8273-26de99416430";
                                                adapter.clear();
                                                Log.i(LOG_TAG, URL);
                                                getLoaderManager().restartLoader(0, null, MainActivity.this);
                                                Log.d(LOG_TAG, "readmore clicked");
                                            } else {
                                                readmore.setVisibility(GONE);
                                            }
                                        }
                                    }

        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.categories_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.world_news:
                SECTION = WORLD;
                count = 10;
                if (checkInternet())
                {
                    readmore.setVisibility(View.VISIBLE);
                    URL = "https://content.guardianapis.com/search?&section=" + SECTION + "&page-size=" + count + "&show-fields=thumbnail,trailText&show-tags=contributor&api-key=6de1a8cd-9a9d-4016-8273-26de99416430";
                    adapter.clear();
                    getLoaderManager().restartLoader(0, null, MainActivity.this);
                }
                return true;
            case R.id.technology_news:
                SECTION = TECHNOLOGY;
                count = 10;
                if (checkInternet())
                {
                    readmore.setVisibility(View.VISIBLE);
                    URL = "https://content.guardianapis.com/search?&section=" + SECTION + "&page-size=" + count + "&show-fields=thumbnail,trailText&show-tags=contributor&api-key=6de1a8cd-9a9d-4016-8273-26de99416430";
                    adapter.clear();
                    getLoaderManager().restartLoader(0, null, MainActivity.this);
                }
                return true;
            case R.id.travel_news:
                SECTION = TRAVEL;
                count = 10;
                if (checkInternet())
                {
                    readmore.setVisibility(View.VISIBLE);
                    URL = "https://content.guardianapis.com/search?&section=" + SECTION + "&page-size=" + count + "&show-fields=thumbnail,trailText&show-tags=contributor&api-key=6de1a8cd-9a9d-4016-8273-26de99416430";
                    adapter.clear();
                    getLoaderManager().restartLoader(0, null, MainActivity.this);
                }
                return true;
            case R.id.culture_news:
                SECTION = CULTURE;
                count = 10;
                if (checkInternet())
                {
                    readmore.setVisibility(View.VISIBLE);
                    URL = "https://content.guardianapis.com/search?&section=" + SECTION + "&page-size=" + count + "&show-fields=thumbnail,trailText&show-tags=contributor&api-key=6de1a8cd-9a9d-4016-8273-26de99416430";
                    adapter.clear();
                    getLoaderManager().restartLoader(0, null, MainActivity.this);
                }
                return true;
            case R.id.business_news:
                SECTION = BUSINESS;
                count = 10;
                if (checkInternet())
                {
                    readmore.setVisibility(View.VISIBLE);
                    URL = "https://content.guardianapis.com/search?&section=" + SECTION + "&page-size=" + count + "&show-fields=thumbnail,trailText&show-tags=contributor&api-key=6de1a8cd-9a9d-4016-8273-26de99416430";
                    adapter.clear();
                    getLoaderManager().restartLoader(0, null, MainActivity.this);
                }
                return true;
            case R.id.sport_news:
                SECTION = SPORT;
                count = 10;
                checkInternet();
                readmore.setVisibility(View.VISIBLE);
                URL = "https://content.guardianapis.com/search?&section=" + SECTION + "&page-size=" + count + "&show-fields=thumbnail,trailText&show-tags=contributor&api-key=6de1a8cd-9a9d-4016-8273-26de99416430";
                adapter.clear();
                getLoaderManager().restartLoader(0, null, MainActivity.this);
                return true;
            case R.id.environment_news:
                SECTION = ENVIRONMENT;
                count = 10;
                if (checkInternet())
                {
                    readmore.setVisibility(View.VISIBLE);
                    URL = "https://content.guardianapis.com/search?&section=" + SECTION + "&page-size=" + count + "&show-fields=thumbnail,trailText&show-tags=contributor&api-key=6de1a8cd-9a9d-4016-8273-26de99416430";
                    adapter.clear();
                    getLoaderManager().restartLoader(0, null, MainActivity.this);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
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
    private boolean checkInternet() {
        info = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = info.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            no_internet.setVisibility(GONE);
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(0, null, this);
            readmore.setVisibility(View.VISIBLE);
            return true;
        } else {
            readmore.setVisibility(View.GONE);
            no_internet.setVisibility(View.VISIBLE);
            loading.setVisibility(GONE);
            return false;
        }
    }
}
