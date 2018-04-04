package kartollika.matrixcalc;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;

public class EditTextDottable extends android.support.v7.widget.AppCompatEditText {
    private boolean hasDot;
    private boolean hasMinus;
    private boolean hasDivider;

    public EditTextDottable(Context context) {
        super(context);
    }

    public EditTextDottable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextDottable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isHasDot() {
        return hasDot;
    }

    public boolean isHasMinus() {
        return hasMinus;
    }

    public boolean isHasDivider() {
        return hasDivider;
    }

    public void delDot() {
        Editable s = getText();
        if (s.charAt(s.length() - 1) == '.') {
            setText(s.delete(s.length() - 1, s.length()));
            setSelection(getText().length());
        } else {
            int pos = getSelectionStart();
            setText(s.delete(pos - 1, pos));
            setSelection(pos - 1);
        }
        hasDot = false;
    }

    public void addDot() {
        int pos = getSelectionStart();
        setText(getText().insert(pos, "."));
        hasDot = true;
        setSelection(pos + 1);
    }

    public void addMinus() {
        int pos = getSelectionStart();
        String s = String.valueOf(getText());
        setText("-" + s);
        hasMinus = true;
        setSelection(pos + 1);
    }

    public void delMinus() {
        int pos = getSelectionStart();
        try {
            setText(getText().delete(0, 1));
        } catch (IndexOutOfBoundsException e) {
        }
        hasMinus = false;
        if (pos == 0) {
            setSelection(pos);
        } else {
            setSelection(pos - 1);
        }
    }

    public void fill(RationalNumber r) {
        setText(r.toString());
        if (r.getNumerator() < 0) {
            hasMinus = true;
        }

        if (r.getDenominator() != 1) {
            hasDot = true;
        }

    }

    public void replaceDot() {
        int pos = getSelectionStart();
        hasDot = false;
        hasDivider = true;
        Editable s = getText();
        setText(s.replace(getSelectionStart() - 1, getSelectionStart(), "/"));
        setSelection(pos);
    }

    public void replaceDivider() {
        int pos = getSelectionStart();
        hasDot = true;
        hasDivider = false;
        Editable s = getText();
        setText(s.replace(getSelectionStart() - 1, getSelectionStart(), "."));
        setSelection(pos);
    }

    public void delDivider() {
        Editable s = getText();
        if (s.charAt(s.length() - 1) == '/') {
            setText(s.delete(s.length() - 1, s.length()));
            setSelection(getText().length());
        } else {
            int pos = getSelectionStart();
            setText(s.delete(pos - 1, pos));
            if (pos == 0) {
                setSelection(pos);
            } else {
                setSelection(pos - 1);
            }
        }
        hasDivider = false;
    }
}
