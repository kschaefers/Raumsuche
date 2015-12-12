package de.hs_mannheim.stud.raumsuche.network.services;

import com.squareup.okhttp.ResponseBody;

import java.util.List;

import de.hs_mannheim.stud.raumsuche.models.User;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by Martin on 12/10/15.
 */
public interface UserService {
    @GET("/users")
    Call<List<User>> listUsers();

    @GET("/users/{studentId}")
    Call<User> getUser(@Path("studentId") String studentId);

    @Headers("Content-Type: application/json")
    @PUT("/register")
    Call<User> createUser(@Body User user);

    @POST("/users/{studentId}")
    Call<ResponseBody> updateUser(@Path("studentId") String studentId, @Body User user);

    @DELETE("/users/{studentId}")
    Call<User> deleteUser(@Path("studentId") String studentId);
}
