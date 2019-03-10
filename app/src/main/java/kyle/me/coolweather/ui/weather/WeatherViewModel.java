package kyle.me.coolweather.ui.weather;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import kyle.me.coolweather.data.Resource;
import kyle.me.coolweather.data.WeatherRepository;
import kyle.me.coolweather.data.model.weather.Weather;

public class WeatherViewModel extends ViewModel {
    public Weather mWeather;
    public String mBingPicUrl;
    private WeatherRepository mWeatherRepository;

    public WeatherViewModel(WeatherRepository repository) {
        this.mWeatherRepository = repository;
    }

    public boolean isWeatherCached() {
        return mWeatherRepository.isWeatherCached();
    }

    public Weather getCachedWeather() {
        return mWeatherRepository.getCachedWeather();
    }

    public LiveData<Resource<Weather>> refreshWeather(String weatherId, String key) {
        return mWeatherRepository.refreshWeather(weatherId, key);
    }

    public LiveData<Resource<Weather>> getWeather(String weatherId, String key) {
        return mWeatherRepository.getWeather(weatherId, key);
    }

    public LiveData<Resource<String>> getBingPicUrl() {
        return mWeatherRepository.getBingPic();
    }
}
