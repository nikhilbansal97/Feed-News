package com.example.android.feednews.sync;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.android.feednews.R;
import com.example.android.feednews.WebViewActivity;
import com.example.android.feednews.pojo.NewsModel;
import com.example.android.feednews.pojo.Response;
import com.example.android.feednews.pojo.Result;
import com.example.android.feednews.rest.ApiClient;
import com.example.android.feednews.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.android.feednews.MainActivity.API_KEY;
import static com.example.android.feednews.MainActivity.FIELDS;
import static com.example.android.feednews.MainActivity.TAGS;

/**
 * Created by nikhil on 12/6/17.
 */

public class NewsTasks {

    public static void getData(final Context context) {
        Log.v("NewsTasks : ","getData() called.");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<NewsModel> call = apiInterface.getNews(API_KEY, "technology", 20, FIELDS, TAGS);
        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, retrofit2.Response<NewsModel> response) {
                NewsModel news = response.body();
                Response response1 = news.getResponse();
                int pages = response1.getPages();
                List<Result> results = response1.getResults();
                for (Result res : results) {
                    createNotification(context, res);
                    }
                Log.v("News : ", Integer.toString(pages));
            }
            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                Log.v("MainActivity : ", "Failed loading data!");
            }
        });
    }

    public static void createNotification(Context context, Result result){
        Log.v("NewsTasks : ","createNotification() called.");
        String title = result.getWebTitle();
        String url = result.getWebUrl();
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentIntent(pendingIntent(context,url))
                .setContentText("Lets check out!")
                .setSmallIcon(R.drawable.icon_app)
                .setAutoCancel(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(444,builder.build());
    }

    public static PendingIntent pendingIntent(Context context, String url){
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url",url);

        return PendingIntent.getActivity(context,234,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
