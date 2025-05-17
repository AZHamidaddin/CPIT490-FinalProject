// app/src/main/java/com/example/cpit490project/models/Showtime.java
package com.example.cpit490project.models;

import java.util.List;

public class Showtime {
    public String place;
    public String experience;
    public List<String> times;

    public Showtime(String place, String experience, List<String> times) {
        this.place      = place;
        this.experience = experience;
        this.times      = times;
    }
}
