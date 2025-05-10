package com.example.aflam.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Timing implements Serializable {
    @SerializedName("Date")
    private String date;

    @SerializedName("Showtimes")
    private List<Showtime> showtimes;

    public String getDate()                   { return date; }
    public List<Showtime> getShowtimes()      { return showtimes; }
}
