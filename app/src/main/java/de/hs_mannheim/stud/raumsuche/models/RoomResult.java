package de.hs_mannheim.stud.raumsuche.models;

import org.parceler.Parcel;

import de.hs_mannheim.stud.raumsuche.models.Room;

/**
 * Created by Martin on 12/3/15.
 */

@Parcel
public class RoomResult {
    long id;
    Room room;
    int availableFrom;
    int availableTo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public int getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(int availableFrom) {
        this.availableFrom = availableFrom;
    }

    public int getAvailableTo() {
        return availableTo;
    }

    public void setAvailableTo(int availableTo) {
        this.availableTo = availableTo;
    }
}
