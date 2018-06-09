package kartollika.matrixcalc;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Pair;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class UpdateCheckerService extends Service {

    NotificationManager notificationManager;

    private boolean notifsAtStartAllowed;
    private boolean calledFromSettings;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        try {
            notifsAtStartAllowed = intent.getExtras().getBoolean("notifsAtStartAllowed");
        } catch (Exception ignored) {
        }
        try {
            calledFromSettings = intent.getExtras().getBoolean("fromSettings", false);
        } catch (Exception ignored) {
        }

        if ((notifsAtStartAllowed && !calledFromSettings) ^ calledFromSettings) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    checkForUpdates();
                }
            }).start();
        }

        return Service.START_NOT_STICKY;
    }

    private void showToast(final Context context, final String message) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Utilities.createLongToast(context, message);
                ((TextView) ((LinearLayout) toast.getView()).getChildAt(0)).setGravity(Gravity.CENTER_HORIZONTAL);
                toast.show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }

    private void checkForUpdates() {
        if (hasConnection()) {

            int curVersionCode = BuildConfig.VERSION_CODE;
            Pair<Integer, String> latestVersion = downloadText();

            if (curVersionCode < latestVersion.first) {
                if (calledFromSettings) {
                    sendNotification(latestVersion.second);
                    showToast(getApplicationContext(), getResources().getString(R.string.update_available) +
                            "\n" + getResources().getString(R.string.update_available_from_settings_part2));
                    stopSelf();
                }
                if (notifsAtStartAllowed) {
                    sendNotification(latestVersion.second);
                }

            } else {
                if (calledFromSettings) {
                    showToast(getApplicationContext(), getResources().getString(R.string.noUpdateAvailable));
                }
            }
        } else {
            showToast(getApplicationContext(), getResources().getString(R.string.conn_error));
        }
        stopSelf();
    }

    private static class LatestVersionGetter extends AsyncTask<Void, String, Object[]> {

        private int versionCode = -1;
        private String versionName = "";

        @Override
        protected Object[] doInBackground(Void... voids) {
            try {
                URL url = new URL("https://rawgit.com/kartollika/Matrix_calculator-Android/master/app/latest_version_new.txt");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(30000);
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String str;
                while ((str = in.readLine()) != null) {
                    String[] parts = str.split(" ");
                    versionCode = Integer.valueOf(parts[0]);
                    versionName = parts[1];
                }
                in.close();
            } catch (Exception e) {
                return null;
            }
            return new Object[]{versionCode, versionName};
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Pair<Integer, String> downloadText() throws NullPointerException {
        Object[] info = new Object[2];
        LatestVersionGetter latestVersionGetter = new LatestVersionGetter();
        try {
            info = latestVersionGetter.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return new Pair<>((int) info[0], (String) info[1]);
    }

    private boolean hasConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private void sendNotification(String latestVersionName) {
        Notification.Builder notifBuilder = new Notification.Builder(getApplicationContext());

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()));
        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        String contentText = getResources().getString(R.string.clickToUpdate);

        notifBuilder.setSmallIcon(R.mipmap.icon);
        notifBuilder.setAutoCancel(true);
        notifBuilder.setContentTitle(getResources().getString(R.string.update_available));
        notifBuilder.setOngoing(false);
        notifBuilder.setContentText(contentText + getResources().getString(R.string.version) + " " + latestVersionName);
        notifBuilder.setContentIntent(pIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notifBuilder.setColor(Color.LTGRAY);
        }
        notificationManager.notify(0, notifBuilder.build());
    }
}
