package kartollika.matrixcalc;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by kartollika on 06.01.2018.
 */

public class SplashScreenActivity extends AppCompatActivity {

    final int SPLASH_TIME_OUT = 1500;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        Intent intent = new Intent(this, UpdateCheckerService.class);
        intent.putExtra("notifsAtStartAllowed", preferences.getBoolean("notifications", true));
        startService(intent);

        ((TextView) findViewById(R.id.version)).setText(App.version);

        int defDimN = Integer.parseInt(preferences.getString("defDimN", "3"));
        int defDimM = Integer.parseInt(preferences.getString("defDimM", "3"));

        if (!((App) getApplication()).thisSession) {
            ((App) getApplication()).thisSession = true;
            for (int i = 0; i < 2; ++i) {
                App.matrices[i] = new Matrix(null, defDimN, defDimM);
            }
            App.systemMatrix = new Matrix(null, null, defDimN, defDimM);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    PermAsk.showPermDialog(getFragmentManager());
                } else {
                    loadMainActivity();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Utilities.createShortToast(getApplicationContext(), R.string.permission_granted).show();
                } else {
                    Utilities.createShortToast(getApplicationContext(), R.string.permission_not_granted).show();
                }
        }
        loadMainActivity();
    }

    private void loadMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
