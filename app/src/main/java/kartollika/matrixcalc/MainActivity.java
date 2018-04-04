package kartollika.matrixcalc;

import android.content.ActivityNotFoundException;
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
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import kartollika.matrixcalc.startfragments.LinearSystemFragment;
import kartollika.matrixcalc.startfragments.OperationsFragment;

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
               /* case R.id.navigation_theory: {
                    if (k == 0) {
                        return true;
                    } else {
                        k = 0;
                    }
                    setChecked(0);
                    *//*if (isCreated) {
                        transaction.setCustomAnimations(R.anim.enter_left, R.anim.exit_right);
                    }*//*
                    transaction.replace(R.id.content, new TheoryFragment()).commit();
                   // transaction.setCustomAnimations(0, 0);

                    return true;
                }*/
                case R.id.navigation_operations: {
                    /*if (isCreated) {
                        if (k == 0) {
                            transaction.setCustomAnimations(R.anim.enter_right, R.anim.exit_left);
                        } else {
                            transaction.setCustomAnimations(R.anim.enter_left, R.anim.exit_right);
                        }
                    }*/
                    if (k == 1) {
                        return true;
                    } else {
                        k = 1;
                    }

                    setChecked(1);
                    Fragment fragment = new OperationsFragment();
                    transaction.replace(R.id.content, fragment).commit();
                    //    transaction.setCustomAnimations(0, 0);

                    return true;
                }
                case R.id.navigation_linearsystem: {
                    if (k == 2) {
                        return true;
                    } else {
                        k = 2;
                    }
                    /*if (isCreated) {
                        transaction.setCustomAnimations(R.anim.enter_right, R.anim.exit_left);
                    }*/
                    setChecked(2);
                    transaction.replace(R.id.content, new LinearSystemFragment()).commit();
                    // transaction.setCustomAnimations(0, 0);

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

        AdView adView = (AdView) findViewById(R.id.adView);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                findViewById(R.id.adText).setVisibility(View.VISIBLE);
            }
        });

        AdRequest adRequest;

        adRequest = new AdRequest.Builder().addTestDevice("8161507EB49B3F33630CF2A74D743868").build();
        adView.loadAd(adRequest);

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
                    Toast.makeText(this, getResources().getString(R.string.gp_not_found), Toast.LENGTH_SHORT).show();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public SharedPreferences getPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(this);
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
    protected void onDestroy() {
        getIntent().removeExtra("operation");
        ((AdView) findViewById(R.id.adView)).destroy();
        super.onDestroy();

        preferences = getPrefs();
        SharedPreferences.Editor editor = preferences.edit();
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            editor.putBoolean("isDarkmode", true);
        } else {
            editor.putBoolean("isDarkmode", false);
        }
        editor.apply();
    }

    private void setChecked(int n) {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        MenuView.ItemView itemView;

        for (int i = 0; i < 3; ++i) {
            // if (i == 0) {
            //itemView = (MenuView.ItemView) navigation.findViewById(R.id.navigation_theory);
            //} else if (i == 1) {
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
}
