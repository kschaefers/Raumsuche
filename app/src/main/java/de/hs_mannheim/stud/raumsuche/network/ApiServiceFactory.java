package de.hs_mannheim.stud.raumsuche.network;

import android.content.Context;

import de.hs_mannheim.stud.raumsuche.network.services.UserService;
import de.hs_mannheim.stud.raumsuche.utils.Config;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Martin on 12/10/15.
 */
public class ApiServiceFactory {

    private static ApiServiceFactory instance;
    private Retrofit retrofit;

    private ApiServiceFactory(Context context) {
        retrofit = new Retrofit.Builder()
                .baseUrl(Config.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiServiceFactory getInstance(Context context) {
        if (instance == null) {
            instance = new ApiServiceFactory(context.getApplicationContext());
        }

        return instance;
    }

    public UserService getUserService() {
        return retrofit.create(UserService.class);
    }

}
