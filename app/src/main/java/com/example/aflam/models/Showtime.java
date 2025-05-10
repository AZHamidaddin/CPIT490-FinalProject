package com.example.aflam.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Showtime implements Serializable {
    @SerializedName("Place")
    private String place;

    @SerializedName("Experiences")
    private List<Experience> experiences;

    public String getPlace()                  { return place; }
    public List<Experience> getExperiences()  { return experiences; }
}
