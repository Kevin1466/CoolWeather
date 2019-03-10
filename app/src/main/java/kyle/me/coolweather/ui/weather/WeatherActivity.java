package kyle.me.coolweather.ui.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import kyle.me.coolweather.MainActivity;
import kyle.me.coolweather.R;
import kyle.me.coolweather.data.Resource;
import kyle.me.coolweather.data.model.weather.Forecast;
import kyle.me.coolweather.data.model.weather.Weather;
import kyle.me.coolweather.util.InjectorUtil;

public class WeatherActivity extends AppCompatActivity {
    private WeatherViewModel mWeatherViewModel;
    private String wId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        mWeatherViewModel = ViewModelProviders.of(this, InjectorUtil.getWeatherModelFactory()).get(WeatherViewModel.class);
        wId = getIntent().getStringExtra("weather_id");
        // TODO: 09/03/2019 check cache
        observeWeather(null, false);
    }

    private void observeWeather(LiveData<Resource<Weather>> liveData, boolean refresh) {
        LiveData<Resource<Weather>> weather = mWeatherViewModel.getWeather(wId, MainActivity.KEY);
        weather.observe(this, new Observer<Resource<Weather>>() {
            @Override
            public void onChanged(Resource<Weather> resource) {
                if (resource.status == Resource.LOADING) {

                } else if (resource.data != null && resource.status == Resource.SUCCESS) {
                    showWeatherInfo(resource.data);
                }
            }
        });
    }

    private void showWeatherInfo(Weather weather) {
        ((TextView) findViewById(R.id.titleCity)).setText(weather.basic.cityName);
        ((TextView) findViewById(R.id.titleUpdateTime)).setText(weather.basic.update.updateTime.split(" ")[1]);
        ((TextView) findViewById(R.id.degreeText)).setText(weather.now.temperature + "℃");
        ((TextView) findViewById(R.id.weatherInfoText)).setText(weather.now.more.info);
        ((TextView) findViewById(R.id.titleCity)).setText(weather.basic.cityName);
        LinearLayout forecastLayout = findViewById(R.id.forecastLayout);
        // TODO: 10/03/2019 why?
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = view.findViewById(R.id.dateText);
            TextView infoText = view.findViewById(R.id.infoText);
            TextView maxText = view.findViewById(R.id.maxText);
            TextView minText = view.findViewById(R.id.minText);
            dateText.setText(forecast.data);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }

        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carwash.info;
        String sport = "运动建议：" + weather.suggestion.sport.info;

        ((TextView) findViewById(R.id.aqiText)).setText(weather.aqi.city.aqi);
        ((TextView) findViewById(R.id.pm25Text)).setText(weather.aqi.city.aqi);
        ((TextView) findViewById(R.id.comfortText)).setText(comfort);
        ((TextView) findViewById(R.id.carWashText)).setText(carWash);
        ((TextView) findViewById(R.id.sportText)).setText(sport);
        findViewById(R.id.weatherLayout).setVisibility(View.VISIBLE);
    }
}