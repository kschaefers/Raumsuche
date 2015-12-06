package de.hs_mannheim.stud.raumsuche.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import de.hs_mannheim.stud.raumsuche.R;
import de.hs_mannheim.stud.raumsuche.models.Room;
import de.hs_mannheim.stud.raumsuche.models.RoomQuery;
import de.hs_mannheim.stud.raumsuche.models.RoomResult;

/**
 * Created by Martin on 12/4/15.
 */
public class DummyData {

    public static RoomResult buildTestResult(Context context, String building) {
        List<String> propertyList = new ArrayList<String>();
        propertyList.add("Lose Bestuhlung");
        propertyList.add("Beamer");
        propertyList.add("Poolraum");

        Room room = new Room();
        room.setIdentifier(building + "212");
        room.setBuilding(building);
        room.setFloor(4);
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
