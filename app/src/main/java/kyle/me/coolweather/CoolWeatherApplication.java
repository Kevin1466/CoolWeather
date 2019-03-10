package kyle.me.coolweather;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

public class CoolWeatherApplication extends Application {
    public static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        appContext = this;
    }
}
