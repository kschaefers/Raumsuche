package de.hs_mannheim.stud.raumsuche.models;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by m.christmann on 03.12.2015.
 */
@Parcel
public class Room {

    String identifier;

    String building;

    int floor;

    int size;

    List<String> roomProperties;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<String> getRoomProperties() {
        return roomProperties;
    }

    public void setRoomProperties(List<String> roomProperties) {
        this.roomProperties = roomProperties;
    }
}
