package com.example.android.feednews;

/**
 * Created by NIKHIL on 14-02-2017.
 */

public class News {

    private String mTitle;
    private String mDescription;
    private String mImageResource;
    private String mDate;
    private String mAuthor;
    private String mUrl;
    private String mSection;

    public News(String title, String description, String imageResource, String date, String author, String section, String url) {
        mTitle = title;
        mDate = date;
        mDescription = description;
        mImageResource = imageResource;
        mAuthor = author;
        mUrl = url;
        mSection = section;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getImageResource() {
        return mImageResource;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getDate() {
        return mDate;
    }

    public String getSection() {
        return mSection;
    }

    public String getUrl() {
        return mUrl;
    }

}
