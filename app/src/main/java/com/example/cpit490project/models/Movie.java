// app/src/main/java/com/example/cpit490project/models/Movie.java
package com.example.cpit490project.models;

import org.json.JSONArray;

public class Movie {
    public String title;
    public String imageUrl;
    public String language;
    public String showtimesUrl;  // NEW
    public JSONArray timings;

    public Movie(String title, String imageUrl, String language,
                 String showtimesUrl, JSONArray timings) {
        this.title        = title;
        this.imageUrl     = imageUrl;
        this.language     = language;
        this.showtimesUrl = showtimesUrl;
        this.timings      = timings;
    }
}
