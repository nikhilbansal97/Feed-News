package com.example.android.feednews;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.feednews.MainActivity.LOG_TAG;

/**
 * Created by NIKHIL on 14-02-2017.
 */

public class QueryUtils {

    private QueryUtils() {
    }

    public static List<News> getNews(String requestUrl) {
        URL url = createURL(requestUrl);
        String response = null;
        try {
            response = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making HTTP request", e);
        }
        List<News> books_list = extractJsonFeatures(response);
        return books_list;
    }

    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null)
            return jsonResponse;
        HttpURLConnection connection = null;
        InputStream stream = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(10000);
            connection.connect();
            if (connection.getResponseCode() == 200) {
                stream = connection.getInputStream();
                jsonResponse = readStream(stream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + connection.getResponseCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (stream != null) {
                stream.close();
            }
        }
        return jsonResponse;
    }

    private static String readStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static URL createURL(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static List<News> extractJsonFeatures(String jsonResponse) {
        if (jsonResponse.isEmpty())
            return null;

        List<News> news = new ArrayList<>();

        try {
            JSONObject news_object = new JSONObject(jsonResponse);
            JSONObject response = news_object.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");
            for (int i = 1; i < results.length(); i++) {
                JSONObject newsItem = results.getJSONObject(i);
                String sectionName = newsItem.getString("sectionName");
                String date = newsItem.getString("webPublicationDate");
                String title = newsItem.getString("webTitle");
                String url = newsItem.getString("webUrl");
                JSONObject fields = newsItem.getJSONObject("fields");
                String trailText = fields.getString("trailText");
                if (trailText.contains("<strong>")) {
                    trailText = trailText.replaceAll("<strong>", "");
                    trailText = trailText.replaceAll("</strong>", "");

                }
                String thumbnail = "http://www.ufscnet.com/wp-content/themes/tisya/images/no-image.jpg";
                if (fields.has("thumbnail")) {
                    thumbnail = fields.getString("thumbnail");
                }
                JSONArray tags = newsItem.getJSONArray("tags");
                String contributor = "";
                if (tags.length() != 0) {
                    JSONObject tags_object = tags.getJSONObject(0);
                    contributor = "By " + tags_object.getString("webTitle");
                }
                news.add(new News(title, trailText, thumbnail, date, contributor, sectionName, url));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return news;
    }

}
