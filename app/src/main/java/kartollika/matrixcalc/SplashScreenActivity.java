package kartollika.matrixcalc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        setContentView(R.layout.activity_splash);

        Intent intent = new Intent(this, UpdateCheckerService.class);
        intent.putExtra("notifsAtStartAllowed", preferences.getBoolean("notifications", true));
        startService(intent);

        ((TextView) findViewById(R.id.version)).setText(GlobalValues.version);

        int defDimN = Integer.parseInt(preferences.getString("defDimN", "3"));
        int defDimM = Integer.parseInt(preferences.getString("defDimM", "3"));

        if (!((GlobalValues) getApplication()).thisSession) {
            ((GlobalValues) getApplication()).thisSession = true;
            for (int i = 0; i < 2; ++i) {
               GlobalValues.matrices[i] = new Matrix(null, defDimN, defDimM);
            }
            GlobalValues.systemMatrix = new Matrix(null, null, defDimN, defDimM);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
