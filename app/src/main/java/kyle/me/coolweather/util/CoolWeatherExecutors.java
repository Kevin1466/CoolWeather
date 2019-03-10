package kyle.me.coolweather.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CoolWeatherExecutors {
    public static ExecutorService diskIO = Executors.newSingleThreadExecutor();
    public static ExecutorService networkIO = Executors.newFixedThreadPool(3);
    public static Executor mainThread = new MainThreadExecutor();

    private static class MainThreadExecutor implements Executor {
        Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable runnable) {
            handler.post(runnable);
        }
    }
}
