package kyle.me.coolweather.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import kyle.me.coolweather.data.WeatherRepository;

public class MainModelFactory implements ViewModelProvider.Factory {
    // 为何耦合一个repository
    private WeatherRepository mWeatherRepository;

    public MainModelFactory(WeatherRepository weatherRepository) {
        mWeatherRepository = weatherRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(mWeatherRepository);
    }
}
