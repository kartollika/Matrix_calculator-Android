package kartollika.matrixcalc;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;

import com.google.android.gms.ads.MobileAds;

public class GlobalValues extends Application {

    public Matrix[] matrices = new Matrix[2];
    public Matrix systemMatrix;

    public boolean thisSession = false;
    public static String version;

    SharedPreferences preferences;

    @Override
    public void onCreate() {
        if (BuildConfig.BUILD_TYPE == "debug") {
            version = BuildConfig.VERSION_NAME + "_" + BuildConfig.BUILD_TYPE;
        } else {
            version = BuildConfig.VERSION_NAME;
        }
        MobileAds.initialize(this, "ca-app-pub-9193176037122415~9633966613");
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        AppCompatDelegate.setDefaultNightMode(preferences.getBoolean("isDarkmode", false) ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate();
    }


}
