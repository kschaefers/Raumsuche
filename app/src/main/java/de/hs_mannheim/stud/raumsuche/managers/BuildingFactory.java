package de.hs_mannheim.stud.raumsuche.managers;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import de.hs_mannheim.stud.raumsuche.R;
import de.hs_mannheim.stud.raumsuche.models.Building;

/**
 * Created by Martin on 12/4/15.
 */
public class BuildingFactory {

    private static BuildingFactory instance;

    private Map<String, Building> buildings;

    private BuildingFactory(Context context) {
        Resources resources = context.getResources();
        TypedArray identifiers = resources.obtainTypedArray(R.array.buildingNames);
        TypedArray locations = resources.obtainTypedArray(R.array.buildingLocations);
        TypedArray colors = resources.obtainTypedArray(R.array.buildingColors);

        buildings = new HashMap<>();

        for (int i = 0; i < identifiers.length(); i++) {
            Building building = new Building();
            building.setIdentifier(identifiers.getString(i));
            String[] latLng = TextUtils.split(locations.getString(i), ",");
            building.setGeoPositionLat(Double.parseDouble(latLng[0]));
            building.setGeoPositionLng(Double.parseDouble(latLng[1]));
            building.setBuildingColor(colors.getColor(i, 0));
            buildings.put(building.getIdentifier(), building);
        }

        identifiers.recycle();
        locations.recycle();
        colors.recycle();
    }

    public static BuildingFactory getInstance(Context context) {
        if (instance == null) {
            instance = new BuildingFactory(context.getApplicationContext());
        }

        return instance;
    }

    public Building getBuildingByIdentifier(String identifier) {
        return buildings.get(identifier);
    }
}
