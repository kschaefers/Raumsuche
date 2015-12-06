package de.hs_mannheim.stud.raumsuche.models;

/**
 * Created by Martin on 12/6/15.
 */
public class Building {
    String identifier;

    int buildingColor;

    double geoPositionLat;

    double geoPositionLng;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getBuildingColor() {
        return buildingColor;
    }

    public void setBuildingColor(int buildingColor) {
        this.buildingColor = buildingColor;
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
}
