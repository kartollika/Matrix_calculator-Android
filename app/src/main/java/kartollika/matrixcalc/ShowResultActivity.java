package kartollika.matrixcalc;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appodeal.ads.Appodeal;

import java.util.List;

import kartollika.matrixcalc.operations.Stepable;
import kartollika.matrixcalc.operations.binaries.ConstMultiply;
import kartollika.matrixcalc.operations.binaries.Minus;
import kartollika.matrixcalc.operations.binaries.Multiply;
import kartollika.matrixcalc.operations.binaries.Sum;
import kartollika.matrixcalc.operations.unaries.Determinant;
import kartollika.matrixcalc.operations.unaries.Inverse;
import kartollika.matrixcalc.operations.unaries.LinearSystem;
import kartollika.matrixcalc.operations.unaries.Power;
import kartollika.matrixcalc.operations.unaries.Transport;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static kartollika.matrixcalc.PermissionRequestUtil.ACCESS_COARSE_LOCATION_CODE;
import static kartollika.matrixcalc.PermissionRequestUtil.WRITE_EXTERNAL_STORAGE_CODE;
import static kartollika.matrixcalc.PermissionRequestUtil.requestPermission;

/*import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;*/

public class ShowResultActivity extends AppCompatActivity implements View.OnClickListener {
    Matrix result;
    int operation;

    Operation oper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Matrix[] inpMatrices;
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);

        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        if (getIntent().getExtras() == null) {
            return;
        }

        Matrix matrix = getIntent().getExtras().getParcelable("matrix");
        Matrix matrix1 = getIntent().getExtras().getParcelable("matrix1");
        Matrix matrix2 = getIntent().getExtras().getParcelable("matrix2");

        operation = getIntent().getIntExtra("operation", -1);

        if (operation > 7 && operation != 10 && operation != 14) {
            Button stepByStep = (Button) findViewById(R.id.steps);
            TextView hints = (TextView) findViewById(R.id.hints);
            hints.setMovementMethod(new ScrollingMovementMethod());
            stepByStep.setVisibility(View.VISIBLE);
            stepByStep.setText(getResources().getString(R.string.step_by_step));
            stepByStep.setOnClickListener(this);

            CardView include = (CardView) findViewById(R.id.include);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(8, 0, 8, 4);
            include.setLayoutParams(layoutParams);
        }

        result = null;

        if (operation < 8 && operation != 2 && operation != 6) {
            inpMatrices = new Matrix[]{matrix1, matrix2};
        } else {
            inpMatrices = new Matrix[]{matrix};
        }

        switch (operation) {
            case 0:
            case 4:
                oper = new Sum(inpMatrices[0], inpMatrices[1]);
                result = oper.calc();
                break;

            case 1:
            case 5:
                oper = new Minus(inpMatrices[0], inpMatrices[1]);
                result = oper.calc();
                break;

            case 2:
            case 6:
                oper = new ConstMultiply(inpMatrices[0],
                        new RationalNumber(
                                getIntent().getLongExtra("constNumerator", 0),
                                getIntent().getLongExtra("constDenominator", 1)
                        )
                );
                result = oper.calc();
                break;

            case 3:
            case 7:
                oper = new Multiply(inpMatrices[0], inpMatrices[1]);
                result = oper.calc();
                break;

            case 8:
            case 12:
                oper = new Determinant(inpMatrices[0], getResources());
                result = oper.calc();
                break;

            case 9:
            case 13:
                Determinant determinant = new Determinant(inpMatrices[0], null);
                determinant.calc();
                if (!determinant.getResultRational().equals(RationalNumber.ZERO)) {
                    oper = new Inverse(inpMatrices[0], getResources());
                    result = oper.calc();
                } else {
                    displayMatrixNotExist();
                    return;
                }
                break;

            case 10:
            case 14:
                oper = new Transport(inpMatrices[0]);
                result = oper.calc();
                break;

            case 11:
            case 15:
                int n = getIntent().getIntExtra("power", 1);
                oper = new Power(inpMatrices[0], n, 1, getResources());
                result = oper.calc();
                break;

            case 16:
                oper = new LinearSystem(inpMatrices[0], getResources());
                result = oper.calc();
                List<String> strings = ((Stepable) oper).getStepStrings();
                findViewById(R.id.hints).setVisibility(View.VISIBLE);
                if (!strings.get(strings.size() - 1).contains("=")) {
                    ((TextView) findViewById(R.id.hints)).setText(getResources().getString(R.string.nothing_to_solve));
                    findViewById(R.id.steps).setEnabled(false);
                } else {
                    ((TextView) findViewById(R.id.hints)).setText(Html.fromHtml(strings.get(strings.size() - 1)));
                }
        }
        new Table(this, result);
    }

    private void displayMatrixNotExist() {
        TextView tv = new TextView(this);
        ((CardView) findViewById(R.id.include)).addView(tv);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(18);
        tv.setTextColor(getResources().getColor(R.color.colorContrast));
        tv.setText(getResources().getString(R.string.matrix_dnexist));
        findViewById(R.id.steps).setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (operation == 16) {
            getMenuInflater().inflate(R.menu.menu_show_result_system, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_show_result, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (result == null) {
            Toast toast = Utilities.createShortToast(this, getResources().getString(R.string.save_to_slot_Fail));
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            v.setGravity(Gravity.CENTER);
            toast.show();
        }

        switch (item.getItemId()) {
            case R.id.saveAsSystem:
                Utilities.createShortToast(this, getResources().getString(R.string.save_to_slot_success)).show();
                result.setEdited();
                GlobalValues.systemMatrix = result;
                break;

            case R.id.saveAsA:
                Utilities.createShortToast(this, getResources().getString(R.string.save_to_slot_1_success)).show();
                result.setEdited();
                GlobalValues.matrices[0] = result;
                break;
            case R.id.saveAsB:
                Utilities.createShortToast(this, getResources().getString(R.string.save_to_slot_2_success)).show();
                result.setEdited();
                GlobalValues.matrices[1] = result;

        }
        return true;
    }

    int step;
    List<Matrix> matrices;
    List<String> strings;
    List<Matrix> extensMatrices;

    @Override
    public void onClick(View view) {
        int ID = view.getId();

        switch (ID) {
            case R.id.steps:
                activateStepByStep();
                break;

            case R.id.prev:
                if (step > 0) {
                    ((ProgressBar) findViewById(R.id.progressBar)).setProgress(--step);
                    ((TextView) findViewById(R.id.hints)).setText(Html.fromHtml(strings.get(step)));
                    if (extensMatrices != null) {
                        new Table(this, matrices.get(step), extensMatrices.get(step));
                    } else {
                        new Table(this, matrices.get(step));
                    }
                }
                break;

            case R.id.next:
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
                if (step < progressBar.getMax()) {
                    progressBar.setProgress(++step);
                    ((TextView) findViewById(R.id.hints)).setText(Html.fromHtml(strings.get(step)));
                    if (extensMatrices != null) {
                        if (step < matrices.size() - 1) {
                            new Table(this, matrices.get(step), extensMatrices.get(step));
                        } else {
                            new Table(this, matrices.get(step));
                        }
                    } else {
                        new Table(this, matrices.get(step));
                    }
                }
        }
    }

    private void activateStepByStepPanel() {
        findViewById(R.id.navigation).setVisibility(View.VISIBLE);
        findViewById(R.id.steps).setVisibility(View.GONE);

        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        findViewById(R.id.prev).setVisibility(View.VISIBLE);
        findViewById(R.id.dummy).setVisibility(View.VISIBLE);
        findViewById(R.id.next).setVisibility(View.VISIBLE);

        findViewById(R.id.hints).setVisibility(View.VISIBLE);

        findViewById(R.id.next).setOnClickListener(this);
        findViewById(R.id.prev).setOnClickListener(this);
    }

    void activateStepByStep() {
        activateStepByStepPanel();
        Stepable operationSteppable = (Stepable) oper;

        matrices = operationSteppable.getStepMatrices();
        strings = operationSteppable.getStepStrings();
        if (operation == 13 || operation == 9) {
            extensMatrices = operationSteppable.getStepExtensionMatrices();
        }

        step = 0;
        ((ProgressBar) findViewById(R.id.progressBar)).setProgress(0);
        ((ProgressBar) findViewById(R.id.progressBar)).setMax(matrices.size() - 1);

        ((TextView) findViewById(R.id.hints)).setText(Html.fromHtml(strings.get(0)));
        if (operation == 9) {
            new Table(this, matrices.get(0), extensMatrices.get(0));
        } else {
            new Table(this, matrices.get(0));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAds();
    }

    private void initAds() {
        attemptGrantPermissionLocation();
        //attemptGrantPermissionWriteExternalStorage();
        AdUtils.loadAd(this);
    }

    private void attemptGrantPermissionWriteExternalStorage() {
        if (!PermissionRequestUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showPermissionExternalStorageWarningDialog();
            }
            PermissionRequestUtil.requestPermission(this,
                    new String[]{WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
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

    private void attemptGrantPermissionLocation() {
        if (!PermissionRequestUtil.checkPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                showPermissionExternalStorageWarningDialog();
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

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Appodeal.destroy(Appodeal.BANNER);
    }
}
