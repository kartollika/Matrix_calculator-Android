package kartollika.matrixcalc;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
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

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import kartollika.matrixcalc.binaryoperations.ConstMultiply;
import kartollika.matrixcalc.binaryoperations.Minus;
import kartollika.matrixcalc.binaryoperations.Multiply;
import kartollika.matrixcalc.binaryoperations.Sum;
import kartollika.matrixcalc.unaryoperations.Determinant;
import kartollika.matrixcalc.unaryoperations.Inverse;
import kartollika.matrixcalc.unaryoperations.LinearSystem;
import kartollika.matrixcalc.unaryoperations.Power;
import kartollika.matrixcalc.unaryoperations.Transport;

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

        AdView adView = (AdView) findViewById(R.id.adView2);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                findViewById(R.id.adText).setVisibility(View.VISIBLE);
            }

        });

        AdRequest adRequest;
        adRequest = new AdRequest.Builder().addTestDevice("8161507EB49B3F33630CF2A74D743868").build();
        adView.loadAd(adRequest);

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
                                getIntent().getLongExtra("constDenominator", 0)
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
                    TextView tv = new TextView(this);
                    ((CardView) findViewById(R.id.include)).addView(tv);
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextSize(18);
                    tv.setTextColor(getResources().getColor(R.color.colorContrast));
                    tv.setText(getResources().getString(R.string.matrix_dnexist));
                    findViewById(R.id.steps).setEnabled(false);
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
                ArrayList<String> strings = (ArrayList<String>) ((kartollika.matrixcalc.unaryoperations.LinearSystem) oper).getObjects()[1];
                findViewById(R.id.hints).setVisibility(View.VISIBLE);
                String s = getResources().getString(R.string.hasSolves) + "<br>";
                if (!strings.get(strings.size() - 1).contains("=")) {
                    ((TextView) findViewById(R.id.hints)).setText(getResources().getString(R.string.nothing_to_solve));
                    findViewById(R.id.steps).setEnabled(false);
                } else {
                    ((TextView) findViewById(R.id.hints)).setText(Html.fromHtml(strings.get(strings.size() - 1)));
                }
        }
        new Table(this, result);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveAsA:
                if (result != null) {
                    Toast.makeText(this, getResources().getString(R.string.save_to_slot_1_success), Toast.LENGTH_SHORT).show();
                    result.setEdited();
                    ((GlobalValues) getApplication()).matrices[0] = result;
                } else {
                    Toast toast = Toast.makeText(this, getResources().getString(R.string.save_to_slot_Fail), Toast.LENGTH_SHORT);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    v.setGravity(Gravity.CENTER);
                    toast.show();
                }
                break;
            case R.id.saveAsB:
                if (result != null) {
                    Toast.makeText(this, getResources().getString(R.string.save_to_slot_2_success), Toast.LENGTH_SHORT).show();
                    result.setEdited();
                    ((GlobalValues) getApplication()).matrices[1] = result;
                } else {
                    Toast toast = Toast.makeText(this, getResources().getString(R.string.save_to_slot_Fail), Toast.LENGTH_SHORT);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    v.setGravity(Gravity.CENTER);
                    toast.show();
                }
        }
        return true;
    }

    int step;
    ArrayList<Matrix> matrices;
    ArrayList<String> strings;
    ArrayList<Matrix> extensMatrices;

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

    void activateStepByStep() {
        findViewById(R.id.navigation).setVisibility(View.VISIBLE);
        findViewById(R.id.steps).setVisibility(View.GONE);

        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        findViewById(R.id.prev).setVisibility(View.VISIBLE);
        findViewById(R.id.dummy).setVisibility(View.VISIBLE);
        findViewById(R.id.next).setVisibility(View.VISIBLE);

        findViewById(R.id.hints).setVisibility(View.VISIBLE);

        findViewById(R.id.next).setOnClickListener(this);
        findViewById(R.id.prev).setOnClickListener(this);

        Object[] objects = null;
        switch (operation) {

                /* DETERMINANT */
            case 8:
            case 12:
                objects = ((Determinant) oper).getObjects();
                break;

                /* INVERSE */
            case 9:
            case 13:
                objects = ((Inverse) oper).getObjects();
                extensMatrices = (ArrayList<Matrix>) objects[2];
                break;

                /* POWER */
            case 11:
            case 15:
                objects = ((Power) oper).getObjects();
                break;

                /* SLAE */
            case 16:
                objects = ((LinearSystem) oper).getObjects();
        }

        matrices = (ArrayList<Matrix>) objects[0];
        strings = (ArrayList<String>) objects[1];

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
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((AdView) findViewById(R.id.adView2)).destroy();
    }
}
