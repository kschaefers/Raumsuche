package de.hs_mannheim.stud.raumsuche.network;

import android.content.Context;
import android.util.Base64;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;

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

    private ApiServiceFactory(Context context) {
        retrofitBuilder = new Retrofit.Builder()
                .baseUrl(Config.baseUrl)
                .addConverterFactory(GsonConverterFactory.create());

        httpClient = new OkHttpClient();
    }

    public static ApiServiceFactory getInstance(Context context) {
        if (instance == null) {
            instance = new ApiServiceFactory(context.getApplicationContext());
        }

        return instance;
    }

    public UserService getUserService() {
        return createService(UserService.class, null, null);
    }

    public UserService getUserService(String studentId, String password) {
        return createService(UserService.class, studentId, password);
    }

    public <S> S createService(Class<S> serviceClass, String username, String password) {
        if (username != null && password != null) {
            String credentials = username + ":" + password;
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            httpClient.interceptors().clear();
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

        Retrofit retrofit = retrofitBuilder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }

}
