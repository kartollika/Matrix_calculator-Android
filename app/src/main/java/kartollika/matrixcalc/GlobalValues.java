package kartollika.matrixcalc;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;

import com.appodeal.ads.Appodeal;

public class GlobalValues extends Application {

    public static Matrix[] matrices = new Matrix[2];
    public static Matrix systemMatrix;
    public static String appKey = "74a017a006898d5e98a9918cff4807f2b6dc172da3ea45aa";
    private static long estimatedTime;


    public boolean thisSession = false;
    public static String version;

    SharedPreferences preferences;

    @Override
    public void onCreate() {

        super.onCreate();
        if (BuildConfig.BUILD_TYPE == "debug") {
            version = BuildConfig.VERSION_NAME + "_" + BuildConfig.BUILD_TYPE;
        } else {
            version = BuildConfig.VERSION_NAME;
        }
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        AppCompatDelegate.setDefaultNightMode(preferences.getBoolean("isDarkmode", false)
                ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        setEstimatedTimeToWatchVideo(preferences.getLong("AdTime", System.currentTimeMillis()));
    }

    public static void setEstimatedTimeToWatchVideo(long time) {
        estimatedTime = time;
    }

    public static long getEstimatedTimeToWatchVideoAd() {
        return estimatedTime;
    }

    public static boolean canShowVideo() {
        return estimatedTime <= System.currentTimeMillis();
    }
}
