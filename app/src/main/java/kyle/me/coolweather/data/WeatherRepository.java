package kyle.me.coolweather.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import kyle.me.coolweather.data.db.WeatherDao;
import kyle.me.coolweather.data.model.weather.HeWeather;
import kyle.me.coolweather.data.model.weather.Weather;
import kyle.me.coolweather.data.network.WeatherNetwork;
import kyle.me.coolweather.util.CoolWeatherExecutors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRepository {
    private static WeatherRepository sWeatherRepository;
    // dependent on weatherDap and weatherNetwork
    private WeatherDao dao;
    private WeatherNetwork network;

    public WeatherRepository(WeatherDao dao, WeatherNetwork network) {
        this.dao = dao;
        this.network = network;
    }

    public static WeatherRepository getInstance(WeatherDao dao, WeatherNetwork network) {
        // double check
        if (sWeatherRepository == null) {
            synchronized (WeatherRepository.class) {
                if (sWeatherRepository == null) {
                    sWeatherRepository = new WeatherRepository(dao, network);
                }
            }
        }
        return sWeatherRepository;
    }

    public LiveData<Resource<Weather>> getWeather(String weatherId, String key) {
        MutableLiveData<Resource<Weather>> liveDate = new MutableLiveData<>();
        liveDate.setValue(new Resource().loading(null));
        return null;
    }

    public LiveData<Resource<Weather>> requestWeather(String weatherId, String key) {
        MutableLiveData<Resource<Weather>> liveDate = new MutableLiveData<>();
        network.fetchWeather(weatherId, key, new Callback<HeWeather>() {
            @Override
            public void onResponse(Call<HeWeather> call, Response<HeWeather> response) {
                CoolWeatherExecutors.diskIO.execute(new Runnable() {
                    @Override
                    public void run() {
                        Weather weather = response.body().weather.get(0);
                        // TODO: 10/03/2019  cache in disk
                        liveDate.postValue(new Resource().success(weather));
                    }
                });
            }

            @Override
            public void onFailure(Call<HeWeather> call, Throwable t) {
                liveDate.postValue(new Resource().error(null, "fetch weather failed"));
            }
        });
        return liveDate;
    }
}
