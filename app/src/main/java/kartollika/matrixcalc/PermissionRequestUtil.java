package kartollika.matrixcalc;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public final class PermissionRequestUtil {

    static final int WRITE_EXTERNAL_STORAGE_CODE = 0;
    static final int ACCESS_COARSE_LOCATION_CODE = 1;

    public static void requestPermission(Activity activity, String[] permissions, int code) {
        ActivityCompat.requestPermissions(activity, permissions, code);
    }

    public static boolean checkPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

}
