package kartollika.matrixcalc;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void showPermissionHelpDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage("")
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        askForPermission();
                    }
                })
                .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                }).create();
        alertDialog.show();
    }

    private void askForPermission() {
       // PermissionRequestUtzil.requestPermissions(this, new String[});
    }
}
