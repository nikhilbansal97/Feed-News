package com.example.android.feednews.pojo;

/**
 * Created by nikhil on 1/6/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fields {

    @SerializedName("trailText")
    @Expose
    private String trailText;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("starRating")
    @Expose
    private String starRating;

    public String getStarRating() {
        return starRating;
    }

    public void setStarRating(String starRating) {
        this.starRating = starRating;
    }

    public String getTrailText() {
        return trailText;
    }

    public void setTrailText(String trailText) {
        this.trailText = trailText;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

}
