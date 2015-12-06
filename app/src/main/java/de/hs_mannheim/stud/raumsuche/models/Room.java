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

     int buildingColor;

     int floor;

     double geoPositionLat;

     double geoPositionLng;

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

    public int getBuildingColor() {
        return buildingColor;
    }

    public void setBuildingColor(int buildingColor) {
        this.buildingColor = buildingColor;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public double getGeoPositionLat() {
        return geoPositionLat;
    }

    public void setGeoPositionLat(double geoPositionLat) {
        this.geoPositionLat = geoPositionLat;
    }

    public double getGeoPositionLng() {
        return geoPositionLng;
    }

    public void setGeoPositionLng(double geoPositionLng) {
        this.geoPositionLng = geoPositionLng;
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
