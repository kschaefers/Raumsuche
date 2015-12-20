package de.hs_mannheim.stud.raumsuche.models;

import org.parceler.Parcel;

/**
 * Created by m.christmann on 03.12.2015.
 */
@Parcel
public class RoomProperties {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
