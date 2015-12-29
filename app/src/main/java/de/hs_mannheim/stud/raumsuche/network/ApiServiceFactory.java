package de.hs_mannheim.stud.raumsuche.network;

import android.util.Base64;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;

import de.hs_mannheim.stud.raumsuche.network.services.GroupService;
import de.hs_mannheim.stud.raumsuche.network.services.MeetingService;
import de.hs_mannheim.stud.raumsuche.network.services.RoomService;
import de.hs_mannheim.stud.raumsuche.network.services.UserService;
import de.hs_mannheim.stud.raumsuche.utils.Config;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Martin on 12/10/15.
 */
public class ApiServiceFactory {

    private static ApiServiceFactory instance;
    private Retrofit.Builder retrofitBuilder;
    private OkHttpClient httpClient;
    private HttpLoggingInterceptor logging;

    private ApiServiceFactory() {
        retrofitBuilder = new Retrofit.Builder()
                .baseUrl(Config.baseUrl)
                .addConverterFactory(GsonConverterFactory.create());

        logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient = new OkHttpClient();
    }

    public static ApiServiceFactory getInstance() {
        if (instance == null) {
            instance = new ApiServiceFactory();
        }

        return instance;
    }

    public UserService getUserService() {
        return createService(UserService.class, null, null);
    }

    public UserService getUserService(String studentId, String password) {
        return createService(UserService.class, studentId, password);
    }

    public GroupService getGroupService(String studentId, String password) {
        return createService(GroupService.class, studentId, password);
    }

    public MeetingService getMeetingService(String studentId, String password) {
        return createService(MeetingService.class, studentId, password);
    }

    public RoomService getRoomService() {
        return createService(RoomService.class, null, null);
    }

    public <S> S createService(Class<S> serviceClass, String username, String password) {
        httpClient.interceptors().clear();

        if (username != null && password != null) {
            String credentials = username + ":" + password;
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            httpClient.interceptors().add(new Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", basic)
                            .header("Accept", "applicaton/json")
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }

        httpClient.interceptors().add(logging);

        Retrofit retrofit = retrofitBuilder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }

}
