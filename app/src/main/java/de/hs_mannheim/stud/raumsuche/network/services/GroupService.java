package de.hs_mannheim.stud.raumsuche.network.services;

import com.squareup.okhttp.ResponseBody;

import java.util.List;

import de.hs_mannheim.stud.raumsuche.models.Group;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by Martin on 12/13/15.
 */
public interface GroupService {

    @GET("/groups")
    Call<List<Group>> listGroups();

    @PUT("/groups")
    Call<Group> createGroup(@Body Group group);

    @GET("/groups/{groupId}")
    Call<Group> getGroup(@Path("groupId") String groupId);

    @POST("/groups/{groupId}")
    Call<ResponseBody> updateGroup(@Path("groupId") String groupId, @Body Group user);

    @DELETE("/groups/{groupId}")
    Call<Group> deleteGroup(@Path("groupId") String groupId);

    @GET("/users/{studentId}/groups")
    Call<List<Group>> listUserGroups(@Path("studentId") String studentId);
}
