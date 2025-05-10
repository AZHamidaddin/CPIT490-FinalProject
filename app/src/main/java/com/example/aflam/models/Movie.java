package com.example.aflam.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Movie implements Serializable {

    @SerializedName("_id")
    private String id;

    @SerializedName("Title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("Image URL")
    private String imageUrl;

    @SerializedName("Language")
    private String language;

    @SerializedName("Genre")
    private List<String> genre;

    @SerializedName("timings")
    private List<String> timings;

    @SerializedName("Parent")
    private String parent;

    // ---- Getters ----
    public String getId()           { return id; }
    public String getTitle()        { return title; }
    public String getDescription()  { return description; }
    public String getImageUrl()     { return imageUrl; }
    public String getLanguage()     { return language; }
    public List<String> getGenre()  { return genre; }
    public List<String> getTimings(){ return timings; }
    public String getParent()       { return parent; }

    // ---- Setter for imageUrl ----
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
