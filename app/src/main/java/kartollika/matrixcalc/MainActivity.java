package kartollika.matrixcalc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.menu.MenuView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdView;

import kartollika.matrixcalc.startfragments.LinearSystemFragment;
import kartollika.matrixcalc.startfragments.OperationsFragment;

import static kartollika.matrixcalc.AdUtils.rewardedVideoAd;

public class MainActivity extends AppCompatActivity {

    Boolean isCreated = false;

    private int k = -1;
    private SharedPreferences preferences;
    private int curNightMode = AppCompatDelegate.getDefaultNightMode();
    private AdView adView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_operations: {
                    if (k == 1) {
                        return true;
                    } else {
                        k = 1;
                    }

                    selectTab(1);
                    Fragment fragment = new OperationsFragment();
                    transaction.replace(R.id.content, fragment).commit();

                    return true;
                }
                case R.id.navigation_linearsystem: {
                    if (k == 2) {
                        return true;
                    } else {
                        k = 2;
                    }
                    setChecked(2);
                    transaction.replace(R.id.content, new LinearSystemFragment()).commit();
                    return true;
                }
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adView = findViewById(R.id.adView);

        AdUtils.initBanner(this);
        AdUtils.initRewardedVideo(this);
        AdUtils.initInterstitialAd(this);

        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        isCreated = false;

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            navigation.setSelectedItemId(R.id.navigation_operations);
        }

        preferences = getPrefs();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("operation", -1);
        editor.putString("operationText", getResources().getString(R.string.choose_operation));
        editor.apply();

        isCreated = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final Activity activity = this;
        switch (item.getItemId()) {
            case R.id.settings: {
                Intent settings = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settings);
                return true;
            }

            case R.id.rate:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + getPackageName()));
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Utilities.createShortToast(this, R.string.gp_not_found).show();
                }
                return true;

            case R.id.remove_ads:
                if (!App.canShowNewBannersVideo() && !App.canShowNewInterstitialVideo()) {
                    Utilities.createLongToast(activity,
                            R.string.error_try_watch_ad_again).show();
                    return true;
                }
                final AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle(R.string.remove_banners)
                        .setMessage(getString(R.string.message_about_removing_ads,
                                App.BLOCKING_BANNERS, App.BLOCKING_INTERSITIALS))
                        .setPositiveButton(R.string.banners, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (App.canShowNewBannersVideo()) {
                                    App.CUR_REWARD = "BANNERS";
                                    AdUtils.showRewardVideoAd(getApplicationContext());
                                } else {
                                    Utilities.createLongToast(MainActivity.this,
                                            R.string.error_try_watch_ad_again).show();
                                }
                                dialog.dismiss();
                            }
                        })
                        .setNeutralButton(R.string.back, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(R.string.interstitials, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (App.canShowNewInterstitialVideo()) {
                                    App.CUR_REWARD = "INTERSTITIAL";
                                    AdUtils.showRewardVideoAd(getApplicationContext());
                                } else {
                                    Utilities.createLongToast(MainActivity.this,
                                            R.string.error_try_watch_ad_again).show();
                                }
                                dialog.dismiss();
                            }
                        })
                        .setCancelable(true)
                        .create();
                dialog.show();
                if (!App.canShowNewBannersVideo()) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }

                if (!App.canShowNewInterstitialVideo()) {
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public SharedPreferences getPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (curNightMode != AppCompatDelegate.getDefaultNightMode()) {
            recreate();
            setChecked(k);
        }

        selectTab(k);

        rewardedVideoAd.resume(this);
    }

    private void selectTab(int tab) {
        switch (tab) {
            case 0: {
                setChecked(0);
                break;
            }
            case 1: {
                setChecked(1);
                break;
            }
            case 2: {
                setChecked(2);
                break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        rewardedVideoAd.pause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        AdUtils.destroyBanner(adView);
        rewardedVideoAd.destroy(this);

        getIntent().removeExtra("operation");

        preferences = getPrefs();
        SharedPreferences.Editor editor = preferences.edit();
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            editor.putBoolean("isDarkmode", true);
        } else {
            editor.putBoolean("isDarkmode", false);
        }
        editor.putLong("bannersEstimatedTime", App.getEstimatedTimeRemovingBanners());
        editor.putLong("interstitialEstimatedTime", App.getEstimatedTimeRemovingInterstitial());
        editor.putInt("interstitialCurOperations", InterstitialShow.getCurOperations());

        editor.apply();
    }

    private void setChecked(int n) {
        BottomNavigationView navigation = findViewById(R.id.navigation);
        MenuView.ItemView itemView;

        for (int i = 0; i < 3; ++i) {
            if (i == 1) {
                itemView = navigation.findViewById(R.id.navigation_operations);
            } else {
                itemView = navigation.findViewById(R.id.navigation_linearsystem);
            }

            if (i == n) {
                itemView.setChecked(true);
            } else {
                itemView.setChecked(false);
            }

        }
    }
}
