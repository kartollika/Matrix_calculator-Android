package kartollika.matrixcalc;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by kartollikaa on 28.05.2018.
 */

public final class Utilities {

    static Toast createShortToast(Context context, String text) {
        return Toast.makeText(context, text, Toast.LENGTH_SHORT);
    }

    static Toast createShortToast(Context context, int resId) {
        return createShortToast(context, context.getResources().getString(resId));
    }

    static Toast createLongToast(Context context, String text) {
        return Toast.makeText(context, text, Toast.LENGTH_LONG);
    }

    static Toast createLongToast(Context context, int resId) {
        return createLongToast(context, context.getResources().getString(resId));
    }
}
