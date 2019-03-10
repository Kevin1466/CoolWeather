package kyle.me.coolweather.data.db;

public class WeatherDatabase {
    private static WeatherDao sWeatherDao;
    private static PlaceDao sPlaceDap;

    private WeatherDatabase() {
    }

    public static PlaceDao getPlaceDao() {
        if (sPlaceDap == null) sPlaceDap = new PlaceDao();
        return sPlaceDap;
    }

    public static WeatherDao getWeatherDao() {
        if (sWeatherDao == null) sWeatherDao = new WeatherDao();
        return sWeatherDao;
    }
}
