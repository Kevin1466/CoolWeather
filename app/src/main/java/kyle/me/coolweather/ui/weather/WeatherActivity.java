package kyle.me.coolweather.ui.weather;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import kyle.me.coolweather.MainActivity;
import kyle.me.coolweather.R;
import kyle.me.coolweather.data.Resource;
import kyle.me.coolweather.data.model.weather.Forecast;
import kyle.me.coolweather.data.model.weather.Weather;
import kyle.me.coolweather.util.InjectorUtil;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

public class WeatherActivity extends AppCompatActivity {
    public WeatherViewModel mWeatherViewModel;
    public SwipeRefreshLayout swipeRefresh;
    public DrawerLayout drawerLayout;
    public String weatherId;
    private Button navButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= LOLLIPOP) {
            Window window = getWindow();
            View decorView = window.getDecorView();
            // TODO: 10/03/2019
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);

        swipeRefresh = findViewById(R.id.swipeRefresh);
        navButton = findViewById(R.id.navButton);
        drawerLayout = findViewById(R.id.drawerLayout);

        mWeatherViewModel = ViewModelProviders.of(this, InjectorUtil.getWeatherModelFactory()).get(WeatherViewModel.class);
        if (mWeatherViewModel.isWeatherCached()) {
            weatherId = mWeatherViewModel.getCachedWeather().basic.weatherId;
            showWeatherInfo(mWeatherViewModel.getCachedWeather());
        } else {
            weatherId = getIntent().getStringExtra("weather_id");
            findViewById(R.id.weatherLayout).setVisibility(View.INVISIBLE);
            observeWeather(mWeatherViewModel.getWeather(weatherId, MainActivity.KEY), false);
        }
        observeBindPic(mWeatherViewModel.getBingPicUrl());
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                observeWeather(mWeatherViewModel.refreshWeather(weatherId, MainActivity.KEY), true);
            }
        });
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void observeBindPic(LiveData<Resource<String>> liveData) {
        liveData.observe(this, new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                ImageView bingPicImg = findViewById(R.id.bingPicImg);
                if (mWeatherViewModel.mBingPicUrl == null) {
                    if (resource.data != null && resource.status == Resource.SUCCESS) {
                        mWeatherViewModel.mBingPicUrl = resource.data;
                        Glide.with(WeatherActivity.this).load(resource.data).into(bingPicImg);
                    } else {
                        Toast.makeText(WeatherActivity.this, resource.msg, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Glide.with(WeatherActivity.this).load(mWeatherViewModel.mBingPicUrl).into(bingPicImg);
                }
            }
        });
    }

    public void observeWeather(LiveData<Resource<Weather>> liveData, boolean refresh) {
        if (mWeatherViewModel.mWeather == null || refresh) {
            // FIXME: 10/03/2019 null exception
            liveData.observe(this, new Observer<Resource<Weather>>() {
                @Override
                public void onChanged(Resource<Weather> resource) {
                    if (resource.status == Resource.LOADING) {

                    } else if (resource.data != null && resource.status == Resource.SUCCESS) {
                        showWeatherInfo(resource.data);
                        swipeRefresh.setRefreshing(false);
                        mWeatherViewModel.mWeather = resource.data;
                    } else {
                        Toast.makeText(WeatherActivity.this, resource.msg, Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(true);
                    }
                }
            });
        } else {
            showWeatherInfo(mWeatherViewModel.mWeather);
            swipeRefresh.setRefreshing(false);
        }
        LiveData<Resource<Weather>> weather = mWeatherViewModel.getWeather(weatherId, MainActivity.KEY);
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