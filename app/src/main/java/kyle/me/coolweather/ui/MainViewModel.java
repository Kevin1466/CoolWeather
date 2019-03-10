package kyle.me.coolweather.ui;

import androidx.lifecycle.ViewModel;
import kyle.me.coolweather.data.WeatherRepository;

public class MainViewModel extends ViewModel {
    private WeatherRepository weatherRepository;

    public MainViewModel(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public boolean isWeatherCached() {
        return weatherRepository.isWeatherCached();
    }
}
