package kartollika.matrixcalc;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.menu.MenuView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.appodeal.ads.Appodeal;

import kartollika.matrixcalc.startfragments.LinearSystemFragment;
import kartollika.matrixcalc.startfragments.OperationsFragment;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static kartollika.matrixcalc.PermissionRequestUtil.ACCESS_COARSE_LOCATION_CODE;
import static kartollika.matrixcalc.PermissionRequestUtil.WRITE_EXTERNAL_STORAGE_CODE;
import static kartollika.matrixcalc.PermissionRequestUtil.requestPermission;

public class MainActivity extends AppCompatActivity {

    int k = -1;
    SharedPreferences preferences;
    Boolean isCreated = false;
    private int curNightMode = AppCompatDelegate.getDefaultNightMode();

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

                    setChecked(1);
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
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        isCreated = false;

        /*AdView adView = (AdView) findViewById(R.id.adView);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                findViewById(R.id.adText).setVisibility(View.VISIBLE);
            }
        });
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("8161507EB49B3F33630CF2A74D743868").build();
        adView.loadAd(adRequest);*/

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
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
                    Utilities.createShortToast(this, getResources().getString(R.string.gp_not_found)).show();
                }
                return true;
            case R.id.remove_ads:
                if (GlobalValues.canShowVideo()) {
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setTitle(R.string.remove_banners)
                            .setMessage(R.string.message_about_removing_banners)
                            .setPositiveButton(getResources().getString(R.string.watch), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AdUtils.loadVideo(activity);
                                }
                            })
                            .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setCancelable(true)
                            .create();
                    dialog.show();
                } else {
                    Utilities.createLongToast(activity,
                            getResources().getString(R.string.error_try_watch_ad_again)).show();
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
        initAds();
    }

    private void initAds() {
        attemptGrantPermissionLocation();
       // attemptGrantPermissionWriteExternalStorage();
        AdUtils.loadAd(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (curNightMode != AppCompatDelegate.getDefaultNightMode()) {
            recreate();
            setChecked(k);
        }

        switch (k) {
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    attemptGrantPermissionLocation();
                }
                break;
            case ACCESS_COARSE_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    attemptGrantPermissionWriteExternalStorage();
                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getIntent().removeExtra("operation");
        Appodeal.destroy(Appodeal.BANNER);

        preferences = getPrefs();
        SharedPreferences.Editor editor = preferences.edit();
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            editor.putBoolean("isDarkmode", true);
        } else {
            editor.putBoolean("isDarkmode", false);
        }
        editor.putLong("AdTime", GlobalValues.getEstimatedTimeToWatchVideoAd());
        editor.apply();
    }

    private void setChecked(int n) {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        MenuView.ItemView itemView;

        for (int i = 0; i < 3; ++i) {
            if (i == 1) {
                itemView = (MenuView.ItemView) navigation.findViewById(R.id.navigation_operations);
            } else {
                itemView = (MenuView.ItemView) navigation.findViewById(R.id.navigation_linearsystem);
            }

            if (i == n) {
                itemView.setChecked(true);
            } else {
                itemView.setChecked(false);
            }

        }
    }

    private void attemptGrantPermissionWriteExternalStorage() {
        if (!PermissionRequestUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showPermissionExternalStorageWarningDialog();
            } else {
                PermissionRequestUtil.requestPermission(this,
                        new String[]{WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
            }
        }
    }

    private void attemptGrantPermissionLocation() {
        if (!PermissionRequestUtil.checkPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                showPermissionLocationWarningDialog();
            } else {
                PermissionRequestUtil.requestPermission(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_COARSE_LOCATION_CODE);
            }
        }
    }

    private void showPermissionExternalStorageWarningDialog() {
        final Activity activity = this;
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.permWarning)
                .setMessage(R.string.externalstoragewarning)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        requestPermission(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                WRITE_EXTERNAL_STORAGE_CODE);
                    }
                })
                .setCancelable(false)
                .create();
        dialog.show();
    }

    private void showPermissionLocationWarningDialog() {
        final Activity activity = this;
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.permWarning)
                .setMessage(R.string.locationwarning)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        requestPermission(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                ACCESS_COARSE_LOCATION_CODE);
                    }
                })
                .setCancelable(false)
                .create();
        dialog.show();
    }
}
