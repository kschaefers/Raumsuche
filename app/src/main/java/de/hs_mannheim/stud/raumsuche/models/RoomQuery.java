package de.hs_mannheim.stud.raumsuche.models;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Martin on 12/4/15.
 */
@Parcel
public class RoomQuery {

    private List<String> properties;

    public List<String> getProperties() {
        return properties;
    }

    public void setProperties(List<String> properties) {
        this.properties = properties;
    }
}
