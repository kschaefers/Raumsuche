package de.hs_mannheim.stud.raumsuche.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin on 12/4/15.
 */
public class DummyData {

    public static RoomResult buildTestResult(Context context, String building) {

        Room room = new Room();
        room.setName(building + "212");
        room.setDay(1);
        room.setHour("4,5");
        room.setSize(80);
        room.setBeamer(true);
        room.setLooseSeating(true);
        room.setPool(true);


        RoomResult roomResult = new RoomResult();
        roomResult.setId(1);
        roomResult.setRoom(room);

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
