package com.example.android.feednews;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.android.feednews.pojo.NewsModel;
import com.example.android.feednews.pojo.Response;
import com.example.android.feednews.pojo.Result;
import com.example.android.feednews.rest.ApiClient;
import com.example.android.feednews.rest.ApiInterface;
import com.example.android.feednews.sync.NewsUtilities;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

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
    private CardView newsCard;
    private CardView readmore;
    private ProgressBar loading;
    private RelativeLayout no_internet;
    private NavigationView mNavigation;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    public static final String API_KEY = "6de1a8cd-9a9d-4016-8273-26de99416430";
    public static final String FIELDS = "starRating,thumbnail,trailText";
    public static final String TAGS = "contributor";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        /**
         * Setup Navigation Drawer
         */
        mNavigation = (NavigationView) findViewById(R.id.navigation);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        mNavigation.setItemIconTintList(null);


        no_internet = (RelativeLayout) findViewById(R.id.no_internet_view);
        news_list = (ListView) findViewById(R.id.list_view);
        readmore = (CardView) findViewById(R.id.readmore);
        readmore.setVisibility(View.VISIBLE);
        loading = (ProgressBar) findViewById(R.id.loading);
        adapter = new NewsAdapter(MainActivity.this, new ArrayList<Result>());
        news_list.setAdapter(adapter);
        adapter.clear();
        news_list.setEmptyView(loading);
        newsCard = (CardView) findViewById(R.id.news_card);

        getData(this, SECTION);
        NewsUtilities.scheduleNews(this);

        news_list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Result current_news = adapter.getItem(position);
                        String url = current_news.getWebUrl();
                        Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                    }
                }
        );

        mNavigation.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.world_news:
                                SECTION = WORLD;
                                break;
                            case R.id.travel_news:
                                SECTION = TRAVEL;
                                break;
                            case R.id.technology_news:
                                SECTION = TECHNOLOGY;
                                break;
                            case R.id.sport_news:
                                SECTION = SPORT;
                                break;
                            case R.id.environment_news:
                                SECTION = ENVIRONMENT;
                                break;
                            case R.id.culture_news:
                                SECTION = CULTURE;
                                break;
                            case R.id.business_news:
                                SECTION = BUSINESS;
                                break;
                            default:
                                Toast.makeText(MainActivity.this, "Item Invalid!", Toast.LENGTH_SHORT).show();
                        }
                        getData(MainActivity.this, SECTION);
                        return true;
                    }
                }
        );
    }

    public void getData(final Context context, final String section) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<NewsModel> call = apiInterface.getNews(API_KEY, section, count, FIELDS, TAGS);
        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, retrofit2.Response<NewsModel> response) {
                NewsModel news = response.body();
                Response response1 = news.getResponse();
                int pages = response1.getPages();
                List<Result> results = response1.getResults();
                adapter = new NewsAdapter(context, results);
                news_list.setAdapter(adapter);
                Log.v("News : ", Integer.toString(pages) + section);
            }
            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                Log.v("MainActivity : ", "Failed loading data!");
            }
        });
    }

}

