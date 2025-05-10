package com.example.aflam.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Experience implements Serializable {
    @SerializedName("Experience")
    private String experience;

    @SerializedName("Times")
    private List<String> times;

    public String getExperience()   { return experience; }
    public List<String> getTimes()  { return times; }
}
