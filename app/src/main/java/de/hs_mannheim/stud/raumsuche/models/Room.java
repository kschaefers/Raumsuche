package de.hs_mannheim.stud.raumsuche.models;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.christmann on 03.12.2015.
 */
@Parcel
public class Room {

    String name;

    int day;

    String hour;

    int size;

    boolean computer;
    boolean beamer;
    boolean pool;
    boolean looseSeating;
    boolean video;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean hasComputer() {
        return computer;
    }

    public void setComputer(boolean computer) {
        this.computer = computer;
    }

    public boolean hasBeamer() {
        return beamer;
    }

    public void setBeamer(boolean beamer) {
        this.beamer = beamer;
    }

    public boolean hasPool() {
        return pool;
    }

    public void setPool(boolean pool) {
        this.pool = pool;
    }

    public boolean hasLooseSeating() {
        return looseSeating;
    }

    public void setLooseSeating(boolean looseSeating) {
        this.looseSeating = looseSeating;
    }

    public boolean hasVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public List<String> getRoomProperties() {
        List<String> list = new ArrayList<>();
        if (hasBeamer()) {
            list.add("Beamer");
        }
        if (hasComputer()) {
            list.add("Computer");
        }
        if (hasLooseSeating()) {
            list.add("Lose Bestuhlung");
        }
        if (hasPool()) {
            list.add("Poolraum");
        }
        if (hasVideo()) {
            list.add("Video");
        }
        return list;
    }

    public String getBuilding() {
        return getName().substring(0, 1);
    }
}
