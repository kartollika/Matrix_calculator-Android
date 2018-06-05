package kartollika.matrixcalc;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public final class PermissionRequestUtil {

    public final static int LOCATION_PERMISSION_CODE = 1;
    public final static int PERMISSIONS_ALL = 123;

    public static void requestPermissions(Activity activity, String[] permissions, int code) {
        if (!checkPermissions(activity.getApplicationContext(), permissions)) {
            ActivityCompat.requestPermissions(activity, permissions, code);
        }
    }

    public static boolean checkPermissions(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}
