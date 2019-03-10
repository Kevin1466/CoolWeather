package kyle.me.coolweather.data.db;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;

import kyle.me.coolweather.CoolWeatherApplication;
import kyle.me.coolweather.data.model.weather.Weather;

public class WeatherDao {
    public void cacheBingPic(String bingPic) {
        if (TextUtils.isEmpty(bingPic)) return;
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(CoolWeatherApplication.appContext).edit();
        editor.putString("bing_pic", bingPic);
        editor.apply();
    }

    public String getCachedBingPic() {
        return PreferenceManager.getDefaultSharedPreferences(CoolWeatherApplication.appContext).getString("bing_pic", null);
    }

    public void cacheWeatherInfo(Weather weather) {
        if (weather == null) return;
        String weatherInfo = new Gson().toJson(weather);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(CoolWeatherApplication.appContext).edit();
        editor.putString("weather", weatherInfo);
        editor.apply();
    }

    public Weather getCachedWeatherInfo() {
        String weatherInfo = PreferenceManager.getDefaultSharedPreferences(CoolWeatherApplication.appContext).getString("weather", null);
        if (!TextUtils.isEmpty(weatherInfo)) {
            return new Gson().fromJson(weatherInfo, Weather.class);
        }
        return null;
    }
}
