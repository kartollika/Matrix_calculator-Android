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

    private void switchTab(int i) {
        if (i == 0) {
            ((Button) findViewById(R.id.binaryBtn)).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
            findViewById(R.id.binaryBtn).setBackgroundColor(getResources().getColor(R.color.colorButtonOperation));

            ((Button) findViewById(R.id.unaryBtn)).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            findViewById(R.id.unaryBtn).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        } else {
            ((Button) findViewById(R.id.unaryBtn)).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
            findViewById(R.id.unaryBtn).setBackgroundColor(getResources().getColor(R.color.colorButtonOperation));

            ((Button) findViewById(R.id.binaryBtn)).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            findViewById(R.id.binaryBtn).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
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
