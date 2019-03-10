package kyle.me.coolweather.data.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ServiceCreator {
    private static final String BASE_URL = "http://guolin.tech/";
    private OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
    private Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClientBuilder.build())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create());
    private Retrofit retrofit = retrofitBuilder.build();

    public <T> T create(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
