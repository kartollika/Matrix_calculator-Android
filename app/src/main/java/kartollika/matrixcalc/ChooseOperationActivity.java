package kartollika.matrixcalc;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import kartollika.matrixcalc.choosingfragments.Binaries;
import kartollika.matrixcalc.choosingfragments.Unaries;

public class ChooseOperationActivity extends AppCompatActivity {

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.OperationsTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_operation);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        int oper = getIntent().getIntExtra("operation", -1);
        if (oper < 8) {
            fragmentTransaction.replace(R.id.container, new Binaries()).commit();
            switchTab(0);
        } else if (oper > 7) {
            fragmentTransaction.replace(R.id.container, new Unaries()).commit();
            switchTab(1);
        }
    }

    private void highlightTab(int id_Highlight, int id_Old) {
        ((Button) findViewById(id_Highlight)).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
        findViewById(id_Highlight).setBackgroundColor(getResources().getColor(R.color.colorButtonOperation));

        ((Button) findViewById(id_Old)).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        findViewById(id_Old).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    private void switchTab(int i) {
        int tab1 = R.id.binaryBtn;
        int tab2 = R.id.unaryBtn;
        if (i == 0) {
            highlightTab(tab1, tab2);
        } else {
            highlightTab(tab2, tab1);
        }
    }

    public void onClick(View view) {
        int ID = view.getId();

        switch (ID) {
            case R.id.binaryBtn:
                fragmentManager = getSupportFragmentManager();
                try {
                    Binaries binaries = (Binaries) fragmentManager.findFragmentById(R.id.container);
                } catch (Exception e) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.enter_left, R.anim.exit_right);
                    fragmentTransaction.replace(R.id.container, new Binaries()).commit();
                    switchTab(0);
                }
                break;

            case R.id.unaryBtn:
                fragmentManager = getSupportFragmentManager();
                try {
                    Unaries unaries = (Unaries) fragmentManager.findFragmentById(R.id.container);
                } catch (Exception e) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.enter_right, R.anim.exit_left);
                    fragmentTransaction.replace(R.id.container, new Unaries()).commit();
                    switchTab(1);
                }

                break;

        }
    }

}
