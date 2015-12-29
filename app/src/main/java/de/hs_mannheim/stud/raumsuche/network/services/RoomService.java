package de.hs_mannheim.stud.raumsuche.network.services;

import java.util.List;
import java.util.Map;

import de.hs_mannheim.stud.raumsuche.models.Room;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.QueryMap;

/**
 * Created by Christoph on 12/14/15.
 */
public interface RoomService {
    @GET("/rooms")
    Call<List<Room>> findRooms(@QueryMap Map<String, String> options);
}
