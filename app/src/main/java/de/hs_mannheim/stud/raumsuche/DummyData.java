package de.hs_mannheim.stud.raumsuche;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin on 12/4/15.
 */
public class DummyData {

    public static RoomResult buildTestResult(Context context) {
        List<String> propertyList = new ArrayList<String>();
        propertyList.add("Lose Bestuhlung");
        propertyList.add("Beamer");
        propertyList.add("Poolraum");

        Room room = new Room();
        room.setIdentifier("A212");
        room.setBuilding("A");
        room.setBuildingColor(context.getResources().getColor(R.color.blue));
        room.setFloor(4);
        room.setGeoPositionLat(49.468486);
        room.setGeoPositionLng(8.482424);
        room.setSize(80);
        room.setRoomProperties(propertyList);

        RoomResult roomResult = new RoomResult();
        roomResult.setId(1);
        roomResult.setRoom(room);
        roomResult.setAvailableFrom(4);
        roomResult.setAvailableTo(5);

        return roomResult;
    }

    public static RoomQuery buildTestQuery(Context context) {
        RoomQuery query = new RoomQuery();
        List<String> searchProperties = new ArrayList<String>();
        searchProperties.add("Beamer");
        query.setProperties(searchProperties);

        return query;
    }
}
