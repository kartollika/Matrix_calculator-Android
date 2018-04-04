package kartollika.matrixcalc;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.widget.Toast;

public class SettingsActivity extends AppCompatPreferenceActivity {

    private int versionClickCounter;
    private int vibrateTimeDefault = 75;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.SettingsThemeDark);
        } else {
            setTheme(R.style.SettingsThemeLight);
        }

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        final CheckBoxPreference experimentalPref = (CheckBoxPreference) findPreference("experimental");
        final SwitchPreference darkmode = (SwitchPreference) findPreference("isDarkmode");

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            darkmode.setChecked(true);
        }

        darkmode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                if (darkmode.isChecked()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    setTheme(R.style.SettingsThemeLight);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    setTheme(R.style.SettingsThemeDark);
                }
                recreate();
                return true;
            }
        });


        experimentalPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                final boolean flag = experimentalPref.isChecked();
                if (!flag) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(preference.getContext());
                    alertDialogBuilder.setTitle(getResources().getString(R.string.experimental));
                    alertDialogBuilder.setMessage(getResources().getString(R.string.confirm_experimenta));

                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.back), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    experimentalPref.setChecked(false);
                                    dialog.cancel();
                                }
                            });
                    alertDialogBuilder.create().show();
                }
                return true;
            }
        });

        findPreference("rateThis").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + getPackageName()));
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.gp_not_found), Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        findPreference("version").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (++versionClickCounter == 3) {
                    try {
                        ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(vibrateTimeDefault);
                    } catch (NullPointerException ignored) {
                    } finally {
                        if (vibrateTimeDefault == 75) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.easter_egg), Toast.LENGTH_SHORT).show();
                        }
                        versionClickCounter = 0;
                        vibrateTimeDefault += 20;
                    }
                }
                return false;
            }
        });

        findPreference("version").setSummary(GlobalValues.version);

    }

    @Override
    protected void onApplyThemeResource(Resources.Theme theme, int resid, boolean first) {
        theme.applyStyle(resid, true);
    }
}
