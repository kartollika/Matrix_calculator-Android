package kartollika.matrixcalc;

import android.app.Activity;
import android.os.Build;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

class Table implements View.OnFocusChangeListener {
    private Activity activity;
    private Matrix matrix;
    private boolean flag;

    Table(Activity activity, Matrix matrix) {
        this.activity = activity;
        this.matrix = matrix;
        init(getMatrix());
    }

    Table(Activity activity, Matrix matrix, Matrix extens) {
        this.activity = activity;
        this.matrix = matrix;
        init(getMatrix(), extens);
    }

    private void init(Matrix matrix) {
        LinearLayout ll = activity.findViewById(R.id.table);
        LayoutInflater li = LayoutInflater.from(activity);
        ll.removeAllViews();
        for (int i = 0; i < matrix.getColumnCount(); ++i) {
            LinearLayout ll1 = new LinearLayout(activity);
            ll1.setOrientation(LinearLayout.VERTICAL);
            ll1.setGravity(Gravity.CENTER);
            ll1.setId(Integer.parseInt("999" + i));

            for (int j = 0; j < matrix.getRowCount(); ++j) {
                ViewGroup ll2 = (FrameLayout) li.inflate(R.layout.table_item, null);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                ll2.setLayoutParams(params);

                ll2.findViewById(R.id.cell).setId(i * 1000 + j);

                final EditTextDottable et = ll2.findViewById(i * 1000 + j);
                et.setOnFocusChangeListener(this);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    et.setShowSoftInputOnFocus(false);
                } else {
                    et.setTextIsSelectable(true);
                }

                ll1.addView(ll2);

                if (activity.getLocalClassName().equals("ShowResultActivity")) {
                    et.setInputType(InputType.TYPE_NULL);
                    et.setFocusable(false);
                }

                RationalNumber r = matrix.getValuesMap().get(i * 1000 + j);
                et.fill(r);

                if (i == j) {
                    setBackgroundDiagonal(et);
                }
            }
            ll.addView(ll1);
        }

        if (matrix.coefs) {
            addAugmented(matrix);
        }

        if (activity.getLocalClassName().equals("InputMatrixActivity")) {
            setNexts(matrix.getRowCount(), matrix.getColumnCount());
        }

    }

    private void setNexts(int rowCount, int columnCount) {
        EditTextDottable et;
        EditTextDottable etNext;
        for (int i = 0; i < columnCount; ++i) {
            for (int j = 0; j < rowCount; ++j) {
                et = activity.findViewById(i * 1000 + j);
                etNext = activity.findViewById((i + 1) * 1000 + j);
                try {
                    et.setNextFocusForwardId(etNext.getId());
                } catch (NullPointerException e) {
                    try {
                        if (isFlag()) {
                            EditTextDottable editTextDottable = activity.findViewById(100_000 + j);
                            et.setNextFocusForwardId(editTextDottable.getId());
                            editTextDottable.setNextFocusForwardId(j + 1);
                        } else {
                            et.setNextFocusForwardId(j + 1);
                        }
                    } catch (NullPointerException e1) {
                        return;
                    }
                }
            }
        }
    }

    private void init(Matrix matrix, Matrix extens) {
        LinearLayout ll = activity.findViewById(R.id.table);
        LayoutInflater li = LayoutInflater.from(activity);
        ll.removeAllViews();
        for (int i = 0; i < getMatrix().getColumnCount() + extens.getColumnCount(); ++i) {
            LinearLayout ll1 = new LinearLayout(activity);
            ll1.setOrientation(LinearLayout.VERTICAL);
            ll1.setGravity(Gravity.CENTER);
            ll1.setId(Integer.parseInt("999" + i));

            for (int j = 0; j < getMatrix().getRowCount(); ++j) {
                ViewGroup ll2 = (FrameLayout) li.inflate(R.layout.table_item, null);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                ll2.setLayoutParams(params);

                ll2.findViewById(R.id.cell).setId(i * 1000 + j);

                EditTextDottable et = ll2.findViewById(i * 1000 + j);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    et.setShowSoftInputOnFocus(false);
                }

                ll1.addView(ll2);

                if (activity.getLocalClassName().equals("ShowResultActivity")) {
                    et.setInputType(InputType.TYPE_NULL);
                    et.setFocusable(false);
                }

                RationalNumber d;
                if (i < getMatrix().getColumnCount()) {
                    d = matrix.getValuesMap().get(i * 1000 + j);
                } else {
                    d = extens.getValuesMap().get((i - matrix.getColumnCount()) * 1000 + j);
                }

                et.fill(d);

                setFlag();

                if (i >= getMatrix().getColumnCount()) {
                    et.setBackground(activity.getResources().getDrawable(R.drawable.back_extens));
                } else {
                    if (i == j) {
                        setBackgroundDiagonal(et);
                    }
                }
            }
            ll.addView(ll1);
        }
    }

    void addRow(int newDimN) {
        LinearLayout ll;
        int k = getMatrix().getRowCount();

        while (k < newDimN) {
            LayoutInflater li = LayoutInflater.from(activity);

            for (int i = 0; i < (!isFlag() ? getMatrix().getColumnCount() : getMatrix().getColumnCount() + 1); ++i) {
                if (i < getMatrix().getColumnCount()) {
                    ll = activity.findViewById(Integer.parseInt("999" + i));
                } else {
                    ll = activity.findViewById(Integer.parseInt("8888" + 1));
                }
                ViewGroup ll1 = (FrameLayout) li.inflate(R.layout.table_item, null);

                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                ll1.setLayoutParams(params);

                EditTextDottable et;
                if (isFlag() && i >= getMatrix().getColumnCount()) {
                    ll1.findViewById(R.id.cell).setId(100_000 + k);
                    et = ll1.findViewById(100_000 + k);
                } else {
                    ll1.findViewById(R.id.cell).setId(i * 1000 + k);
                    et = ll1.findViewById(i * 1000 + k);
                }
                ll.addView(ll1);
                et.setOnFocusChangeListener(this);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    et.setShowSoftInputOnFocus(false);
                }

                et.setText(String.valueOf(0));

                if (isFlag() && i >= getMatrix().getColumnCount()) {
                    et.setBackground(activity.getResources().getDrawable(R.drawable.back_extens));
                    continue;
                }

                if (i == k) {
                    setBackgroundDiagonal(et);
                }
            }
            ++k;
        }
        setNexts(newDimN, getMatrix().getColumnCount());
    }

    void addColumn(int newDimM) {
        int k = getMatrix().getColumnCount();
        int colCount = k;

        while (k < newDimM) {
            LayoutInflater li = LayoutInflater.from(activity);
            LinearLayout ll = activity.findViewById(R.id.table);

            LinearLayout ll1 = new LinearLayout(activity);
            ll1.setOrientation(LinearLayout.VERTICAL);
            ll1.setGravity(Gravity.CENTER);
            ll1.setId(Integer.parseInt("999" + k));

            for (int i = 0; i < getMatrix().getRowCount(); ++i) {
                ViewGroup ll2 = (FrameLayout) li.inflate(R.layout.table_item, null);

                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                ll2.setLayoutParams(params);

                ll2.findViewById(R.id.cell).setId(k * 1000 + i);

                ll1.addView(ll2);

                EditTextDottable et = ll1.findViewById(k * 1000 + i);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    et.setShowSoftInputOnFocus(false);
                }
                et.setOnFocusChangeListener(this);

                et.setText(String.valueOf(0));

                if (i == k) {
                    setBackgroundDiagonal(et);
                }
            }

            if (!isFlag()) {
                ll.addView(ll1);
            } else {
                ll.addView(ll1, colCount++);
            }

            ++k;
        }
        setNexts(getMatrix().getRowCount(), newDimM);
    }

    void deleteRow(int newDimN) {
        int k = getMatrix().getRowCount();
        while (k > newDimN) {
            for (int i = 0; i < (!isFlag() ? getMatrix().getColumnCount() : getMatrix().getColumnCount() + 1); ++i) {
                if (i < getMatrix().getColumnCount()) {
                    ((LinearLayout) activity.findViewById(Integer.parseInt("999" + i))).removeViewAt(k - 1);
                } else {
                    ((LinearLayout) activity.findViewById(Integer.parseInt("8888" + 1))).removeViewAt(k - 1);
                }
            }
            --k;
        }
        setNexts(newDimN, getMatrix().getColumnCount());
    }

    void deleteColumn(int newDimM) {
        int k;
        k = matrix.getColumnCount();

        while (k > newDimM) {
            ((LinearLayout) activity.findViewById(R.id.table)).removeViewAt(--k);
        }
        setNexts(getMatrix().getRowCount(), newDimM);
    }

    private void setBackgroundDiagonal(EditTextDottable et) {
        et.setBackground(activity.getResources().getDrawable(R.drawable.back_diagonal));
    }

    public Matrix getMatrix() {
        return matrix;
    }

    private void addAugmented(Matrix matrix) {
        setFlag();

        LayoutInflater li = LayoutInflater.from(activity);
        LinearLayout ll = activity.findViewById(R.id.table);

        LinearLayout ll1 = new LinearLayout(activity);
        ll1.setOrientation(LinearLayout.VERTICAL);
        ll1.setGravity(Gravity.CENTER);
        ll1.setId(Integer.parseInt("8888" + 1));

        for (int i = 0; i < getMatrix().getRowCount(); ++i) {
            ViewGroup ll2 = (FrameLayout) li.inflate(R.layout.table_item, null);

            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            ll2.setLayoutParams(params);

            ll2.findViewById(R.id.cell).setId(100_000 + i);
            ll1.addView(ll2);

            EditTextDottable et = ll1.findViewById(100_000 + i);
            et.setOnFocusChangeListener(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                et.setShowSoftInputOnFocus(false);
            }
            if (activity.getLocalClassName().equals("ShowResultActivity")) {
                et.setInputType(InputType.TYPE_NULL);
                et.setFocusable(false);
            }

            RationalNumber d = matrix.getCoefsMap().get(100_000 + i);
            et.fill(d);

            et.setBackground(activity.getResources().getDrawable(R.drawable.back_extens));
        }
        ll.addView(ll1);
    }

    boolean isFlag() {
        return flag;
    }

    private void setFlag() {
        this.flag = true;
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        EditTextDottable et = (EditTextDottable) view;
        if (b) {
            if ("0".equals(String.valueOf(et.getText()))) {
                et.setText("");
            }
        } else {
            if ("".equals(String.valueOf(et.getText()))) {
                et.setText("0");
            }
        }
    }
}
