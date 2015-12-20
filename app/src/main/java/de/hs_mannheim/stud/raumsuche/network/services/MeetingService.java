package de.hs_mannheim.stud.raumsuche.network.services;

import com.squareup.okhttp.ResponseBody;

import java.util.List;

import de.hs_mannheim.stud.raumsuche.models.Meeting;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by Christoph on 12/14/15.
 */
public interface MeetingService {
    @GET("/meetings")
    Call<List<Meeting>> listMeetings();

    @GET("/meetings/{meetingId}")
    Call<Meeting> getMeeting(@Path("meetingId") String meetingId);

    @PUT("/meetings")
    Call<Meeting> createMeeting(@Body Meeting meeting);

    @POST("/meetings/{meetingId}")
    Call<ResponseBody> updateMeeting(@Path("meetingId") String meetingId, @Body Meeting meeting);

    @DELETE("/meetings/{meetingId}")
    Call<Meeting> deleteMeeting(@Path("meetingId") String meetingId);

    @GET("/groups/{groupId}/meetings")
    Call<List<Meeting>> listGroupMeetings(@Path("groupId") String groupId);
}
