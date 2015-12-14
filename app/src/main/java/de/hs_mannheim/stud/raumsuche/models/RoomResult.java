package de.hs_mannheim.stud.raumsuche.models;

import android.text.TextUtils;
import android.util.Log;

import org.parceler.Parcel;

import de.hs_mannheim.stud.raumsuche.models.Room;

/**
 * Created by Martin on 12/3/15.
 */

@Parcel
public class RoomResult {
    long id;
    Room room;
    String available;

    public String getAvailable() {
        String[] hourArray = TextUtils.split(room.getHour(), ",");
        String available = "";
        for (int i = 0; i < hourArray.length; i++) {
            int current = Integer.parseInt(hourArray[i]);
            int next = i < hourArray.length - 1 ? Integer.parseInt(hourArray[i + 1]) : Integer.parseInt(hourArray[i]);
            int previous = i > 0 ? Integer.parseInt(hourArray[i - 1]) : Integer.parseInt(hourArray[i]);

            if (next == current + 1) {
                if (previous != current - 1) {
                    if (!available.equals("")) {
                        available += ", " + current;
                    } else {
                        available += current;
                    }
                }
            } else {
                if (previous != current - 1) {
                    if (!available.equals("")) {
                        available += ", " + current + ". Block";
                    } else {
                        available += current + ". Block";
                    }
                } else {
                    available += " - " + current + ". Block";
                }
            }
        }
        return available;
    }

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
}
