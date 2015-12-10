package de.hs_mannheim.stud.raumsuche.network;

import android.content.Context;

import de.hs_mannheim.stud.raumsuche.utils.Config;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Martin on 12/10/15.
 */
public class ApiService {

    private static ApiService instance;
    private Retrofit retrofit;

    private ApiService(Context context) {
        retrofit = new Retrofit.Builder()
                .baseUrl(Config.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiService getInstance(Context context) {
        if (instance == null) {
            instance = new ApiService(context.getApplicationContext());
        }

        return instance;
    }

}
