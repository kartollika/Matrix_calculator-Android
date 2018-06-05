package kartollika.matrixcalc;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;

import com.google.android.gms.ads.MobileAds;

public class App extends Application {

    public boolean thisSession = false;
    public static String version;
    public static Matrix[] matrices = new Matrix[2];
    public static Matrix systemMatrix;
    public static final String TARGET_DEVICE_ID = "8161507EB49B3F33630CF2A74D743868";
    public static String CUR_REWARD;
    public static final int BLOCKING_BANNERS = 90;
    public static final int BLOCKING_INTERSITIALS = 60;

    private static long estimatedTimeRemovingBanners;
    private static long estimatedTimeRemovingInterstitial;
    private SharedPreferences preferences;
    private static final String APP_ID = "ca-app-pub-9193176037122415~9633966613";

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.BUILD_TYPE == "debug") {
            version = BuildConfig.VERSION_NAME + "_" + BuildConfig.BUILD_TYPE;
        } else {
            version = BuildConfig.VERSION_NAME;
        }

        MobileAds.initialize(getApplicationContext(), APP_ID);
        AdUtils.initResources(this);


        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        AppCompatDelegate.setDefaultNightMode(preferences.getBoolean("isDarkmode", false)
                ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        InterstitialShow.CUR_OPERATIONS = preferences.getInt("interstitialCurOperations", 1);
        setEstimatedTimeBanners(preferences.getLong("bannersEstimatedTime", System.currentTimeMillis()));
        setEstimatedTimeInterstitial(preferences.getLong("interstitialEstimatedTime", System.currentTimeMillis()));
    }

    public static void setEstimatedTimeBanners(long time) {
        estimatedTimeRemovingBanners = time;
    }

    public static long getEstimatedTimeRemovingBanners() {
        return estimatedTimeRemovingBanners;
    }

    public static long getEstimatedTimeRemovingInterstitial() {
        return estimatedTimeRemovingInterstitial;
    }

    public static void setEstimatedTimeInterstitial(long time) {
        estimatedTimeRemovingInterstitial = time;
    }

    public static boolean canShowNewBannersVideo() {
        return estimatedTimeRemovingBanners <= System.currentTimeMillis();
    }

    public static boolean canShowNewInterstitialVideo() {
        return estimatedTimeRemovingInterstitial <= System.currentTimeMillis();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            editor.putBoolean("isDarkmode", true);
        } else {
            editor.putBoolean("isDarkmode", false);
        }

        /*editor.putLong("bannersEstimatedTime", estimatedTimeRemovingBanners);
        editor.putLong("interstitialEstimatedTime", estimatedTimeRemovingInterstitial);*/
        editor.apply();
    }
}
