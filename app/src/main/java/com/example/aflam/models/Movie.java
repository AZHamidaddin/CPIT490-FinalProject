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
    private List<String> genre;        // ← add this

    @SerializedName("Parent")
    private String parent;

    @SerializedName("Timings")
    private List<Timing> timings;

    // ---- Getters ----
    public String getId()              { return id; }
    public String getTitle()           { return title; }
    public String getDescription()     { return description; }
    public String getImageUrl()        { return imageUrl; }
    public String getLanguage()        { return language; }
    public List<String> getGenre()     { return genre; }      // ← and this
    public String getParent()          { return parent; }
    public List<Timing> getTimings()   { return timings; }

    // ---- Setters (used by flatten step) ----
    public void setImageUrl(String url)      { this.imageUrl = url; }
    public void setTimings(List<Timing> t)   { this.timings = t; }
}
