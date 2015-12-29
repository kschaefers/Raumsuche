package de.hs_mannheim.stud.raumsuche.models;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Martin on 12/4/15.
 */
@Parcel
public class RoomQuery {

    public RoomQuery() {
        properties = new ArrayList<String>();
    }

    private Date searchDate;

    private List<String> properties;

    public List<String> getProperties() {
        return properties;
    }

    public void setProperties(List<String> properties) {
        this.properties = properties;
    }

    public Date getSearchDate() {
        if (searchDate == null) {
            return new Date();
        }

        return searchDate;
    }

    public void setSearchDate(Date searchDate) {
        this.searchDate = searchDate;
    }
}
