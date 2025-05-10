package com.example.aflam.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class OffersResponse {
    @SerializedName("count")
    private int count;

    @SerializedName("offers")
    private List<Offer> offers;

    public int getCount() {
        return count;
    }

    public List<Offer> getOffers() {
        return offers;
    }
}
