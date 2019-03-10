package kyle.me.coolweather;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import kyle.me.coolweather.ui.MainViewModel;
import kyle.me.coolweather.ui.weather.WeatherActivity;
import kyle.me.coolweather.util.InjectorUtil;

public class MainActivity extends AppCompatActivity {
    public static final String KEY = "45dd25f63300445e967b461d2037e4f9";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (TextUtils.isEmpty(KEY)) {
            new AlertDialog.Builder(this)
                    .setMessage("请先在MainActivity中配置天气API的Key")
                    .setCancelable(false)
                    .setPositiveButton("确定", (anInterface, i) -> finish())
                    .show();
        } else {
            MainViewModel mainViewModel = ViewModelProviders.of(this, InjectorUtil.getMainModelFactory()).get(MainViewModel.class);
            // 为了写一个判断缓存的逻辑，用了ViewModel；
            // ViewModel又需要一个ModelFactory;
            // ModelFactory又是由一个依赖注入工具类创建的
            if (mainViewModel.isWeatherCached()) {
                startActivity(new Intent(this, WeatherActivity.class));
                finish();
            }
        }
    }

}
