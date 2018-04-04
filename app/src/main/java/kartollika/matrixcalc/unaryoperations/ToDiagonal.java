package kartollika.matrixcalc.unaryoperations;

import android.content.res.Resources;

import java.util.ArrayList;

import kartollika.matrixcalc.Matrix;
import kartollika.matrixcalc.R;
import kartollika.matrixcalc.RationalNumber;

import static kartollika.matrixcalc.unaryoperations.LinesOperations.diffLines;
import static kartollika.matrixcalc.unaryoperations.LinesOperations.multiplyLine;
import static kartollika.matrixcalc.unaryoperations.LinesOperations.swapLines;

public class ToDiagonal extends UO {

    private ArrayList<Matrix> matrices;
    private ArrayList<String> strings;
    private ArrayList<Matrix> extensMatrices;
    private Matrix result;
    private int transpositions;

    ToDiagonal(Matrix m, Matrix extens, Resources resources) {
        super(m, extens, resources);
    }

    @Override
    public Matrix calc() {
        boolean flag = false;
        Resources res = getRes();

        if (res != null) {
            matrices = new ArrayList<>();
            strings = new ArrayList<>();
        }

        Matrix k = new Matrix(getM().getValuesMap(), getM().getCoefsMap(), getM().getRowCount(), getM().getColumnCount());
        if (getM().getCoefsMap() == null) {
            k.setCoefs(false);
        }

        Matrix extens = getExtens();
        if (extens != null) {
            extensMatrices = new ArrayList<>();
            flag = true;
            extensMatrices.add(new Matrix(extens.getValuesMap(), extens.getRowCount(), extens.getColumnCount()));
        }

        int iter = 0;
        int notNull;
        boolean isFirst1;

        if (res != null) {
            matrices.add(getM());
            strings.add("");
        }

        while (iter < Math.min(k.getColumnCount(), k.getRowCount())) {
            notNull = iter;
            isFirst1 = false;

            for (int i = iter; i < k.getRowCount(); ++i) {
                if ((k.getValuesMap().get(iter * 1000 + iter)).equals(RationalNumber.ONE)
                        || k.getValuesMap().get(iter * 1000 + iter).equals(RationalNumber.ONE.changeSign())) {
                    isFirst1 = true;
                    break;
                }

                if (!k.getValuesMap().get(iter * 1000 + i).equals(RationalNumber.ZERO)) {
                    if (RationalNumber.abs(k.getValuesMap().get(iter * 1000 + i)).equals(RationalNumber.ONE)) {
                        notNull = i;
                        break;
                    }
                    notNull = i;
                }
            }

            if ((!isFirst1 && RationalNumber.abs(k.getValuesMap().get(iter * 1000 + notNull)).equals(RationalNumber.ONE))
                    || !k.getValuesMap().get(iter * 1000 + notNull).equals(RationalNumber.ZERO) && k.getValuesMap().get(iter * 1000 + iter).equals(RationalNumber.ZERO)) {
                if (notNull != iter) {
                    doSwap(flag, k, extens, iter, notNull);
                }
            }

            for (int i = iter + 1; i < k.getRowCount(); ++i) {
                if (k.getValuesMap().get(iter * 1000 + i).equals(RationalNumber.ZERO)) {
                    continue;
                }
                doDiff(flag, k, extens, iter, i);
            }
            iter++;
        }
        result = k;
        return k;
    }

    private void doSwap(boolean flag, Matrix k, Matrix extens, int line1, int line2) {
        if (flag) {
            swapLines(k, extens, line1, line2);
            if (getRes() != null) {
                extensMatrices.add(new Matrix(extens.getValuesMap(), extens.getRowCount(), extens.getColumnCount()));
            }
        } else {
            swapLines(k, line1, line2);
        }
        transpositions++;

        if (getRes() != null) {
            // matrices.add(new Matrix(k));
            if (k.isCoefs()) {
                matrices.add(new Matrix(k.getValuesMap(), k.getCoefsMap(), k.getRowCount(), k.getColumnCount()));
            } else {
                matrices.add(new Matrix(k.getValuesMap(), k.getRowCount(), k.getColumnCount()));
            }
            strings.add(getRes().getString(R.string.swap, line1 + 1, line2 + 1));
        }
    }

    void doDiff(boolean flag, Matrix k, Matrix extens, int line1, int line2) {
        RationalNumber coef = k.getValuesMap().get(line1 * 1000 + line2)
                .multiply(k.getValuesMap().get(line1 * 1000 + line1).inverse());

        if (flag) {
            diffLines(k, extens, line1, line2, coef);
            if (getRes() != null) {
                extensMatrices.add(new Matrix(extens.getValuesMap(), extens.getRowCount(), extens.getColumnCount()));
            }
        } else {
            diffLines(k, line1, line2, coef);
        }
        if (getRes() != null) {
            if (k.isCoefs()) {
                matrices.add(new Matrix(k.getValuesMap(), k.getCoefsMap(), k.getRowCount(), k.getColumnCount()));
            } else {
                matrices.add(new Matrix(k.getValuesMap(), k.getRowCount(), k.getColumnCount()));
            }
            strings.add(getRes().getString(R.string.diff, line2 + 1, line1 + 1, coef.toString()));
        }
    }

    void doMultiply(boolean flag, Matrix k, Matrix extens, int line) {
        RationalNumber coef = k.getValuesMap().get(line * 1000 + line).inverse();
        if (flag) {
            multiplyLine(k, extens, coef, line);
            if (getRes() != null) {
                extensMatrices.add(new Matrix(extens.getValuesMap(),
                        extens.getRowCount(), extens.getColumnCount()));
            }
        } else {

            multiplyLine(k, coef, line);
        }

        if (getRes() != null) {
            if (k.isCoefs()) {
                matrices.add(new Matrix(k.getValuesMap(), k.getCoefsMap(), k.getRowCount(), k.getColumnCount()));
            } else {
                matrices.add(new Matrix(k.getValuesMap(), k.getRowCount(), k.getColumnCount()));
            }
            getStrings().add(getRes().getString(R.string.multi, line + 1, coef.toString()));
        }
    }

    ArrayList<Matrix> getMatrices() {
        return matrices;
    }

    ArrayList<Matrix> getExtensMatrices() {
        return extensMatrices;
    }


    ArrayList<String> getStrings() {
        return strings;
    }

    public Matrix getResult() {
        return result;
    }

    public int getTranspositions() {
        return transpositions;
    }
}
