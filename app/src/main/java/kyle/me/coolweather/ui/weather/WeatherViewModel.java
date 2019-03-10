package kyle.me.coolweather.ui.weather;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import kyle.me.coolweather.data.Resource;
import kyle.me.coolweather.data.WeatherRepository;
import kyle.me.coolweather.data.model.weather.Weather;

public class WeatherViewModel extends ViewModel {
    private Weather mWeather;
    private WeatherRepository mWeatherRepository;

    public WeatherViewModel(WeatherRepository repository) {
        this.mWeatherRepository = repository;
    }

    public LiveData<Resource<Weather>> getWeather(String weatherId, String key) {
        return mWeatherRepository.requestWeather(weatherId, key);
    }
}
