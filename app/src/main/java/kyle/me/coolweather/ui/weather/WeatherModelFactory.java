package kyle.me.coolweather.ui.weather;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import kyle.me.coolweather.data.WeatherRepository;

public class WeatherModelFactory extends ViewModelProvider.NewInstanceFactory {
    private WeatherRepository weatherRepository;

    public WeatherModelFactory(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new WeatherViewModel(weatherRepository);
    }
}
