package kartollika.matrixcalc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class InputMatrixActivity extends Activity implements
        View.OnClickListener {

    boolean isUpperCardHide = false;
    boolean isUpperCardFullyHide = false;
    boolean isNotificationRequired = true;

    boolean experimental;
    int defDimN;
    int defDimM;

    Keyboard mKeyboard;
    KeyboardView mKeyboardView;

    AlertDialog dialog;

    SharedPreferences settings;

    Matrix matrix;
    Table table;

    TextView rows_cnt;
    TextView cols_cnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_input_matrix);

        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if (!isTablet) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        Button row_Minus = (Button) findViewById(R.id.row_minus);
        Button row_plus = (Button) findViewById(R.id.row_plus);
        Button column_minus = (Button) findViewById(R.id.column_minus);
        Button column_plus = (Button) findViewById(R.id.column_plus);
        Button createE = (Button) findViewById(R.id.createE);
        Button create0 = (Button) findViewById(R.id.create0);
        Button saveButton = (Button) findViewById(R.id.saveButton);
        Button rowCount = (Button) findViewById(R.id.rowCount);
        Button columnCount = (Button) findViewById(R.id.columnCount);
        Button hideUpperCard = (Button) findViewById(R.id.hideCard);

        HorizontalScrollView hsv = (HorizontalScrollView) findViewById(R.id.horScroll);
        hsv.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        hsv.setFocusable(true);
        hsv.setFocusableInTouchMode(true);
        hsv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });

        ScrollView sv = (ScrollView) findViewById(R.id.verScroll);
        sv.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        sv.setFocusable(true);
        sv.setFocusableInTouchMode(true);
        sv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });

        row_Minus.setOnClickListener(this);
        row_plus.setOnClickListener(this);
        column_minus.setOnClickListener(this);
        column_plus.setOnClickListener(this);
        createE.setOnClickListener(this);
        create0.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        rowCount.setOnClickListener(this);
        columnCount.setOnClickListener(this);
        hideUpperCard.setOnClickListener(this);

        this.mKeyboard = new Keyboard(this, R.xml.keys);
        mKeyboardView = (KeyboardView) findViewById(R.id.kv);
        mKeyboardView.setKeyboard(mKeyboard);
        mKeyboardView.setOnKeyboardActionListener(new KeyboardView.OnKeyboardActionListener() {
            @Override
            public void onPress(int i) {

            }

            @Override
            public void onRelease(int i) {

            }

            @Override
            public void onKey(int i, int[] ints) {
                long l = System.currentTimeMillis();
                EditTextDottable et;
                KeyEvent keyEvent;

                switch (i) {
                    /* NEXT */
                    case Keyboard.KEYCODE_DONE:
                        try {
                            et = (EditTextDottable) getCurrentFocus();
                        } catch (ClassCastException e) {
                            break;
                        }

                        try {
                            findViewById(et.getNextFocusForwardId()).requestFocus();
                        } catch (NullPointerException e) {
                            break;
                        }
                        break;

                        /* MINUS PLUS */
                    case 69:
                        try {
                            et = (EditTextDottable) getCurrentFocus();
                        } catch (ClassCastException e) {
                            break;
                        }

                        if (et != null) {
                            if (et.isHasMinus()) {
                                et.delMinus();
                            } else {
                                et.addMinus();
                            }
                        }
                        break;

                        /* DOT / DIVIDER */
                    case 56:
                        try {
                            et = (EditTextDottable) getCurrentFocus();
                        } catch (ClassCastException e) {
                            break;
                        }
                        if (et != null) {
                            Editable s = et.getText();
                            if (et.isHasDot() || et.isHasDivider()) {
                                if (s.charAt(et.getSelectionStart() - 1) == '.') {
                                    et.replaceDot();
                                    break;
                                } else if (s.charAt(et.getSelectionStart() - 1) == '/') {
                                    et.replaceDivider();
                                    break;
                                }
                            }
                            if (!et.isHasDot() && !et.isHasDivider()) {
                                et.addDot();
                            }
                        }
                        break;

                        /* DELETE */
                    case 67:
                        try {
                            et = (EditTextDottable) getCurrentFocus();
                        } catch (ClassCastException e) {
                            break;
                        }

                        Editable s = et.getText();
                        if (et.getSelectionStart() == 0 || et.getText().length() == 0) {
                            break;
                        }

                        if (s.charAt(et.getSelectionStart() - 1) == '.') {
                            et.delDot();
                            break;
                        }

                        if (s.charAt(et.getSelectionStart() - 1) == '-') {
                            et.delMinus();
                            break;
                        }

                        if (s.charAt(et.getSelectionStart() - 1) == '/') {
                            et.delDivider();
                            break;
                        }

                        keyEvent = new KeyEvent(l, l, 0, i, 0, 0, 0, 0, 6);
                        dispatchKeyEvent(keyEvent);
                        break;

                    default:
                        keyEvent = new KeyEvent(l, l, 0, i, 0, 0, 0, 0, 6);
                        dispatchKeyEvent(keyEvent);
                }
            }

            @Override
            public void onText(CharSequence charSequence) {

            }

            @Override
            public void swipeLeft() {

            }

            @Override
            public void swipeRight() {

            }

            @Override
            public void swipeDown() {

            }

            @Override
            public void swipeUp() {

            }
        });

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        isNotificationRequired = settings.getBoolean("isNotifAllScreenRequired", true);
        experimental = settings.getBoolean("experimental", false);
        String strDimM = settings.getString("defDimM", "3");
        String strDimN = settings.getString("defDimN", "3");
        defDimN = Integer.parseInt(strDimN);
        defDimM = Integer.parseInt(strDimM);

        final Intent intent = getIntent();

        matrix = intent.getExtras().getParcelable("matrix");
        if (matrix == null) {
            matrix = intent.getExtras().getParcelable("matrixSys");
        }

        rows_cnt = ((TextView) findViewById(R.id.rows));
        cols_cnt = ((TextView) findViewById(R.id.columns));

        rows_cnt.setText(getResources().getString(R.string.count_of_rows, matrix.getRowCount()));
        cols_cnt.setText(getResources().getString(R.string.count_of_columns, matrix.getColumnCount()));

        table = new Table(this, matrix);

        ((Button) findViewById(R.id.rowCount)).setText(String.valueOf(matrix.getRowCount()));
        ((Button) findViewById(R.id.columnCount)).setText(String.valueOf(matrix.getColumnCount()));

        hideUpperCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final ViewGroup rl = (ViewGroup) findViewById(R.id.rl1);
                if (!isUpperCardFullyHide) {

                    AlphaAnimation alpha = new AlphaAnimation(1.F, 0.F);
                    alpha.setDuration(150);
                    alpha.setFillAfter(true);
                    alpha.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            rl.setVisibility(View.GONE);

                            CardView tabl = (CardView) findViewById(R.id.include);
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tabl.getLayoutParams();
                            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                            tabl.setLayoutParams(layoutParams);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    rl.startAnimation(alpha);


                    isUpperCardFullyHide = true;
                    ((Button) v).setText(getResources().getString(R.string.show_card));
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        final Button rowCount = (Button) findViewById(R.id.rowCount);
        Button columnCount = (Button) findViewById(R.id.columnCount);
        switch (v.getId()) {
            case R.id.row_minus:
                if (matrix.getRowCount() > 1) {
                    table.deleteRow(matrix.getRowCount() - 1);
                    matrix.setRowCount(matrix.getRowCount() - 1);
                    ((Button) findViewById(R.id.rowCount)).setText(String.valueOf(matrix.getRowCount()));
                }
                rows_cnt.setText(getResources().getString(R.string.count_of_rows, matrix.getRowCount()));
                break;

            case R.id.row_plus:
                if (experimental || matrix.getRowCount() < 12) {
                    table.addRow(matrix.getRowCount() + 1);
                    matrix.setRowCount(matrix.getRowCount() + 1);
                } else {
                    Toast toast = Toast.makeText(this,
                            getResources().getString(R.string.big_dims_inform),
                            Toast.LENGTH_SHORT);

                    TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
                    textView.setGravity(Gravity.CENTER);
                    toast.show();
                }
                rows_cnt.setText(getResources().getString(R.string.count_of_rows, matrix.getRowCount()));
                ((Button) findViewById(R.id.rowCount)).setText(String.valueOf(matrix.getRowCount()));
                break;

            case R.id.column_minus:
                if (matrix.getColumnCount() > 1) {
                    table.deleteColumn(matrix.getColumnCount() - 1);
                    matrix.setColumnCount(matrix.getColumnCount() - 1);
                    ((Button) findViewById(R.id.columnCount)).setText(String.valueOf(matrix.getColumnCount()));
                    cols_cnt.setText(getResources().getString(R.string.count_of_columns, matrix.getColumnCount()));
                }
                break;

            case R.id.column_plus:
                if (experimental || matrix.getColumnCount() < 12) {
                    table.addColumn(matrix.getColumnCount() + 1);
                    matrix.setColumnCount(matrix.getColumnCount() + 1);
                } else {
                    Toast toast = Toast.makeText(this,
                            getResources().getString(R.string.big_dims_inform),
                            Toast.LENGTH_SHORT);

                    TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
                    textView.setGravity(Gravity.CENTER);
                    toast.show();
                }
                cols_cnt.setText(getResources().getString(R.string.count_of_columns, matrix.getColumnCount()));
                ((Button) findViewById(R.id.columnCount)).setText(String.valueOf(matrix.getColumnCount()));
                break;

            case R.id.createE: {
                if (hasWindowFocus()) {
                    getCurrentFocus().clearFocus();
                }
                WeakReference<Context> reference = new WeakReference<Context>(this);
                for (int i = 0; i < matrix.getColumnCount(); ++i) {
                    new ThreadSetE(reference, matrix).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, i);
                }
                break;
            }

            case R.id.create0: {
                if (hasWindowFocus()) {
                    getCurrentFocus().clearFocus();
                }
                WeakReference<Context> reference = new WeakReference<Context>(this);
                for (int i = 0; i < matrix.getColumnCount(); ++i)
                    new ThreadSet0(reference, matrix).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, i);
                break;
            }

            case R.id.saveButton:
                Intent intent = new Intent();
                if (saveData()) {
                    intent.putExtra("matrix", new Matrix(matrix));
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;

            case R.id.rowCount:
                dialog = showAlertDialog(rowCount);
                dialog.show();
                break;

            case R.id.columnCount:
                dialog = showAlertDialog(columnCount);
                dialog.show();
                break;

            case R.id.hideCard:
                if (isUpperCardFullyHide) {
                    final ViewGroup rl = (ViewGroup) findViewById(R.id.rl1);
                    AlphaAnimation alpha = new AlphaAnimation(0.F, 1.F);
                    alpha.setDuration(150);
                    alpha.setFillAfter(true);
                    alpha.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            rl.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            rl.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    rl.startAnimation(alpha);
                    isUpperCardFullyHide = false;
                    CardView tabl = (CardView) findViewById(R.id.include);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tabl.getLayoutParams();
                    layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                    layoutParams.addRule(RelativeLayout.BELOW, R.id.rl1);
                    tabl.setLayoutParams(layoutParams);
                    isNotificationRequired = false;

                    if (!isUpperCardHide) {
                        ((Button) v).setText(getResources().getString(R.string.hide_card));
                    } else {
                        ((Button) v).setText(getResources().getString(R.string.show_card));
                    }
                    break;
                }

                if (!isUpperCardHide) {
                    AlphaAnimation alpha = new AlphaAnimation(1.F, 0.F);
                    alpha.setDuration(150);
                    alpha.setFillAfter(true);
                    final ViewGroup rl = (ViewGroup) findViewById(R.id.controlLayout);
                    alpha.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            rl.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    rl.startAnimation(alpha);
                    isUpperCardHide = true;
                    ((Button) v).setText(getResources().getString(R.string.show_card));
                    if (isNotificationRequired) {
                        Toast.makeText(this, getResources().getString(R.string.hide_all_views),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    final ViewGroup rl = (ViewGroup) findViewById(R.id.controlLayout);
                    AlphaAnimation alpha = new AlphaAnimation(0.F, 1.F);
                    alpha.setDuration(150);
                    alpha.setFillAfter(true);
                    alpha.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            rl.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            rl.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    rl.startAnimation(alpha);
                    isUpperCardHide = false;
                    ((Button) v).setText(getResources().getString(R.string.hide_card));
                }
        }
    }

    private AlertDialog showAlertDialog(final Button countOf) {
        LayoutInflater li = LayoutInflater.from(this);
        View dialogView = li.inflate(R.layout.dimension_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        final int whatIsThis;
        final NumberPicker np = (NumberPicker) dialogView.findViewById(R.id.Picker);


        if (countOf.getId() == R.id.rowCount) {
            whatIsThis = 0;
        } else {
            whatIsThis = 1;
        }

        final int temp = (whatIsThis == 0) ? matrix.getRowCount() : matrix.getColumnCount();

        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setTitle(getResources().getString(R.string.inp_dimens));

        np.setMinValue(1);
        np.setWrapSelectorWheel(false);
        if (!experimental) {
            np.setMaxValue(12);
        } else {
            np.setMaxValue(30);
        }
        np.setValue(temp);

        alertDialogBuilder
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int saveValue = np.getValue();
                        if (saveValue > temp) {
                            if (whatIsThis == 0) {
                                table.addRow(saveValue);
                                matrix.setRowCount(saveValue);
                                rows_cnt.setText(getResources().getString(R.string.count_of_rows, matrix.getRowCount()));
                            } else {
                                table.addColumn(saveValue);
                                matrix.setColumnCount(saveValue);
                                cols_cnt.setText(getResources().getString(R.string.count_of_columns, matrix.getColumnCount()));
                            }
                        } else {
                            if (whatIsThis == 0) {
                                table.deleteRow(saveValue);
                                matrix.setRowCount(saveValue);
                                rows_cnt.setText(getResources().getString(R.string.count_of_rows, matrix.getRowCount()));
                            } else {
                                table.deleteColumn(saveValue);
                                matrix.setColumnCount(saveValue);
                                cols_cnt.setText(getResources().getString(R.string.count_of_columns, matrix.getColumnCount()));
                            }
                        }
                        countOf.setText(String.valueOf(saveValue));
                        dialog.dismiss();
                    }
                })

                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return alertDialogBuilder.create();
    }

    private static class ThreadSetE extends AsyncTask<Integer, Integer, Void> {
        private WeakReference<Context> mWeakContext;
        Matrix matrix;

        ThreadSetE(WeakReference<Context> reference, Matrix matrix) {
            mWeakContext = reference;
            this.matrix = matrix;
        }

        @Override
        protected Void doInBackground(Integer... params) {
            int column = params[0];
            for (int i = 0; i < matrix.getRowCount(); ++i) {
                if (column == i) {
                    publishProgress(i, column, 1);
                } else {
                    publishProgress(i, column, 0);
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values[2] == 1) {
                ((EditTextDottable) ((Activity) mWeakContext.get()).findViewById(values[1] * 1000 + values[0])).fill(RationalNumber.ONE);
            } else {
                ((EditTextDottable) ((Activity) mWeakContext.get()).findViewById(values[1] * 1000 + values[0])).fill(RationalNumber.ZERO);
            }
        }
    }

    private static class ThreadSet0 extends AsyncTask<Integer, Integer, Void> {
        private WeakReference<Context> mWeakContext;
        Matrix matrix;

        ThreadSet0(WeakReference<Context> reference, Matrix matrix) {
            mWeakContext = reference;
            this.matrix = matrix;
        }

        @Override
        protected Void doInBackground(Integer... params) {
            int column = params[0];
            for (int i = 0; i < matrix.getRowCount(); ++i) {
                publishProgress(i, column);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            ((EditTextDottable) ((Activity) mWeakContext.get()).findViewById(values[1] * 1000 + values[0])).fill(RationalNumber.ZERO);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }

    private boolean saveData() {
        SparseArray<RationalNumber> valuesMap = new SparseArray<>();
        SparseArray<RationalNumber> coefsMap;

        for (int i = 0; i < matrix.getColumnCount(); ++i) {
            for (int j = 0; j < matrix.getRowCount(); ++j) {
                try {
                    valuesMap.put(i * 1000 + j,
                            RationalNumber.parseRational(
                                    String.valueOf(
                                            (((EditTextDottable) findViewById(i * 1000 + j))
                                                    .getText()))));
                } catch (NullPointerException e) {
                    valuesMap.put(i * 1000 + j, new RationalNumber(0, 1));
                } catch (NumberFormatException e1) {
                    Toast.makeText(this,
                            getResources().getString(R.string.invalid_value,
                                    j + 1, i + 1), Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }
        matrix.setValuesMap(valuesMap);

        coefsMap = new SparseArray<>();
        if (table.isFlag()) {
            coefsMap = new SparseArray<>();
            for (int i = 0; i < matrix.getRowCount(); ++i) {
                try {
                    coefsMap.put(100_000 + i, RationalNumber.parseRational(
                            (String.valueOf(
                                    ((EditTextDottable) findViewById(100_000 + i))
                                            .getText()))));
                } catch (NullPointerException e) {
                    coefsMap.put(100_000 + i, new RationalNumber(0, 1));
                } catch (NumberFormatException e1) {
                    Toast.makeText(this,
                            getResources().getString(R.string.invalid_value,
                                    matrix.getColumnCount() + 1, i + 1), Toast.LENGTH_LONG).show();
                    return false;
                }
            }
            matrix.coefs = true;
            matrix.setCoefsMap(coefsMap);
            return true;
        }

        matrix.coefs = false;
        for (int i = 0; i < matrix.getRowCount(); ++i) {
            coefsMap.put(100_000 + i, RationalNumber.ZERO);
        }
        matrix.setCoefsMap(coefsMap);

        matrix.setEdited();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (dialog != null) {
            dialog.dismiss();
        }
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("isNotifAllScreenRequired", isNotificationRequired);
        editor.apply();

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (dialog != null) {
            dialog.dismiss();
        }
    }
}


