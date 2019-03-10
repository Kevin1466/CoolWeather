package kyle.me.coolweather.data;

import android.text.TextUtils;

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
    private WeatherDao weatherDao;
    private WeatherNetwork network;

    public WeatherRepository(WeatherDao weatherDao, WeatherNetwork network) {
        this.weatherDao = weatherDao;
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

    public LiveData<Resource<String>> getBingPic() {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        CoolWeatherExecutors.diskIO.execute(new Runnable() {
            @Override
            public void run() {
                // TODO: 10/03/2019 try to get pic from cache
                String picUrl = weatherDao.getCachedBingPic();
                if (TextUtils.isEmpty(picUrl)) {
                    requestBingPic(liveData);
                } else {
                    liveData.postValue(new Resource<String>().success(picUrl));
                }
            }
        });
        return liveData;
    }

    private void requestBingPic(MutableLiveData<Resource<String>> liveData) {
        network.fetchBingPic(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // TODO: 10/03/2019 do cache
                String picUrl = response.body();
                weatherDao.cacheBingPic(picUrl);
                liveData.postValue(new Resource<String>().success(picUrl));
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                liveData.postValue(new Resource().error(null, "fetch picture failed"));
            }
        });
    }

    public boolean isWeatherCached() {
        return weatherDao.getCachedWeatherInfo() != null;
    }

    public Weather getCachedWeather() {
        return weatherDao.getCachedWeatherInfo();
    }

    public LiveData<Resource<Weather>> refreshWeather(String weatherId, String key) {
        MutableLiveData<Resource<Weather>> liveDate = new MutableLiveData<>();
        liveDate.setValue(new Resource().loading(null));
        requestWeather(weatherId, key, liveDate);
        return liveDate;
    }

    public LiveData<Resource<Weather>> getWeather(String weatherId, String key) {
        MutableLiveData<Resource<Weather>> liveDate = new MutableLiveData<>();
        liveDate.setValue(new Resource().loading(null));
        CoolWeatherExecutors.diskIO.execute(new Runnable() {
            @Override
            public void run() {
                Weather weatherInfo = weatherDao.getCachedWeatherInfo();
                if (weatherInfo == null) {
                    requestWeather(weatherId, key, liveDate);
                } else {
                    liveDate.postValue(new Resource<Weather>().success(weatherInfo));
                }
            }
        });
        return liveDate;
    }

    public LiveData<Resource<Weather>> requestWeather(String weatherId, String key, MutableLiveData<Resource<Weather>> liveDate) {
        network.fetchWeather(weatherId, key, new Callback<HeWeather>() {
            @Override
            public void onResponse(Call<HeWeather> call, Response<HeWeather> response) {
                CoolWeatherExecutors.diskIO.execute(new Runnable() {
                    @Override
                    public void run() {
                        Weather weather = response.body().weather.get(0);
                        // TODO: 10/03/2019  cache in disk
                        weatherDao.cacheWeatherInfo(weather);
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
