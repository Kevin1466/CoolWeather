package kyle.me.coolweather.util;

import kyle.me.coolweather.data.PlaceRepository;
import kyle.me.coolweather.data.WeatherRepository;
import kyle.me.coolweather.data.db.WeatherDatabase;
import kyle.me.coolweather.data.network.WeatherNetwork;
import kyle.me.coolweather.ui.MainModelFactory;
import kyle.me.coolweather.ui.area.ChooseAreaModelFactory;
import kyle.me.coolweather.ui.weather.WeatherModelFactory;

/**
 * TODO 用dagger2重构
 */
public class InjectorUtil {
    private static MainModelFactory sMainModelFactory;
    private static WeatherModelFactory sWeatherModelFactory;
    private static ChooseAreaModelFactory sChooseAreaModelFactory;
    private static WeatherRepository sWeatherRepository;

    public static MainModelFactory getMainModelFactory() {
        if (sMainModelFactory == null) {
            sMainModelFactory = new MainModelFactory(getWeatherRepository());
        }
        return sMainModelFactory;
    }

    public static ChooseAreaModelFactory getChooseAreaModelFactory() {
        if (sChooseAreaModelFactory == null) {
            sChooseAreaModelFactory = new ChooseAreaModelFactory(getPlaceRepository());
        }
        return sChooseAreaModelFactory;
    }

    public static WeatherModelFactory getWeatherModelFactory() {
        if (sWeatherModelFactory == null) {
            sWeatherModelFactory = new WeatherModelFactory(getWeatherRepository());
        }
        return sWeatherModelFactory;
    }

    private static PlaceRepository getPlaceRepository() {
        return PlaceRepository.getInstance(WeatherDatabase.getPlaceDao(), WeatherNetwork.getInstance());
    }

    private static WeatherRepository getWeatherRepository() {
        return WeatherRepository.getInstance(WeatherDatabase.getWeatherDao(), WeatherNetwork.getInstance());
    }
}
