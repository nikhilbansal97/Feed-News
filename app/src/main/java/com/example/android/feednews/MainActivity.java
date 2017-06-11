package com.example.android.feednews;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    private ListView news_list;
    private int count = 10;
    private NewsAdapter adapter;
    private int REQUEST_DATA = 1;
    private int REQUEST_NOTIFICAION = 2;
    private String CULTURE = "culture";
    private String TECHNOLOGY = "technology";
    private String TRAVEL = "travel";
    private String WORLD = "world";
    private String ENVIRONMENT = "environment";
    private String BUSINESS = "business";
    private String SPORT = "sport";
    private String SECTION = WORLD;
    private String URL = "https://content.guardianapis.com/search?&section=" + SECTION + "&page-size=" + count + "&show-fields=starRating,thumbnail,trailText&show-tags=contributor&api-key=6de1a8cd-9a9d-4016-8273-26de99416430";
    private CardView newsCard;
    private CardView readmore;
    private ProgressBar loading;
    private RelativeLayout no_internet;
    ConnectivityManager info;
    NetworkInfo activeNetwork;
    private NavigationView mNavigation;
    public static final String LOG_TAG = NewsLoader.class.getName();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    public static final String API_KEY = "6de1a8cd-9a9d-4016-8273-26de99416430";
    public static final String FIELDS = "thumbnail,trailText";
    public static final String TAGS = "contributor";

    private NetworkBroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        // Initialize Broadcast Receiver
        broadcastReceiver = new NetworkBroadcastReceiver();
        // Initialize IntentFilter
        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_MANAGE_NETWORK_USAGE);

        mNavigation = (NavigationView) findViewById(R.id.navigation);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
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
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        mNavigation.setItemIconTintList(null);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getData(this,SECTION,REQUEST_DATA);

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
                        switch(item.getItemId())
                        {
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
                        getData(MainActivity.this,SECTION,REQUEST_DATA);
                        return true;
                    }
                }
        );
    }

    public void getData(final Context context, final String section , final int requestCode){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<NewsModel> call = apiInterface.getNews(API_KEY, section, count, FIELDS, TAGS);
        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, retrofit2.Response<NewsModel> response) {
                NewsModel news = response.body();
                Response response1 = news.getResponse();
                int pages = response1.getPages();
                List<Result> results = response1.getResults();
                if(requestCode == REQUEST_DATA){
                    adapter = new NewsAdapter(context, results);
                    news_list.setAdapter(adapter);
                } else if(requestCode == REQUEST_NOTIFICAION){
                    for(Result res : results){
                        String star = res.getFields().getStarRating();
                        if(star.equals("5")){
                            createNotification(context,res);
                        }
                    }
                }
                Log.v("News : ", Integer.toString(pages) + section);
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                Log.v("MainActivity : ", "Failed loading data!");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    public class NetworkBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            getData(context,WORLD,REQUEST_NOTIFICAION);
        }
    }

    public void createNotification(Context context, Result result){

        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setContentTitle("Hot News!!")
                .setContentText("Lets check out!")
                .setSmallIcon(R.drawable.icon_app)
                .setAutoCancel(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(444,builder.build());
    }

}

//    private boolean checkInternet() {
//        info = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        activeNetwork = info.getActiveNetworkInfo();
//        if (activeNetwork != null && activeNetwork.isConnected()) {
//            no_internet.setVisibility(GONE);
//            LoaderManager loaderManager = getLoaderManager();
//            loaderManager.initLoader(0, null, this);
//            readmore.setVisibility(View.VISIBLE);
//            return true;
//        } else {
//            readmore.setVisibility(View.GONE);
//            no_internet.setVisibility(View.VISIBLE);
//            loading.setVisibility(GONE);
//            return false;
//        }
//    }