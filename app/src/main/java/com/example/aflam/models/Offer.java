// app/src/main/java/com/example/aflam/models/Offer.java
package com.example.aflam.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Offer implements Serializable {
    @SerializedName("_id")
    private String id;

    // matches your Mongo field "offer title"
    @SerializedName("offer title")
    private String title;

    // matches your Mongo field "offer_image"
    @SerializedName("offer_image")
    private String imageUrl;

    // matches your Mongo field "offer URL"
    @SerializedName("offer URL")
    private String url;

    // matches your Mongo field "parent"
    @SerializedName("parent")
    private String parent;

    // --- Getters ---
    public String getId()       { return id; }
    public String getTitle()    { return title; }
    public String getImageUrl() { return imageUrl; }
    public String getUrl()      { return url; }
    public String getParent()   { return parent; }
}
