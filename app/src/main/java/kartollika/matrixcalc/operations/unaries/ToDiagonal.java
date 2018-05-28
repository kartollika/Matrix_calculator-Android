package kartollika.matrixcalc.operations.unaries;

import android.content.res.Resources;

import kartollika.matrixcalc.Matrix;
import kartollika.matrixcalc.R;
import kartollika.matrixcalc.RationalNumber;
import kartollika.matrixcalc.operations.SteppableOperationUnary;

public class ToDiagonal extends SteppableOperationUnary {

    private Matrix result;
    private int transpositions;
    private boolean doingSteps = false;

    ToDiagonal(Matrix m, Matrix extens, Resources resources) {
        super(m, extens, resources);
    }

    @Override
    public Matrix calc() {
        boolean flag = false;
        Resources res = getRes();

        if (res != null) {
            doingSteps = true;
        }

        Matrix k = new Matrix(getM().getValuesMap(), getM().getCoefsMap(), getM().getRowCount(), getM().getColumnCount());
        if (getM().getCoefsMap() == null) {
            k.setCoefs(false);
        }

        Matrix extens = getExtens();
        if (extens != null) {
            flag = true;
            stepExtensionMatrices.add(new Matrix(extens.getValuesMap(), extens.getRowCount(), extens.getColumnCount()));
        }

        int iter = 0;
        int notNull;
        boolean isFirst1;

        if (doingSteps) {
            stepMatrices.add(getM());
            stepStrings.add("");
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
            LinesOperations.swapLines(k, extens, line1, line2);
            if (getRes() != null) {
                stepExtensionMatrices.add(new Matrix(extens.getValuesMap(), extens.getRowCount(), extens.getColumnCount()));
            }
        } else {
            LinesOperations.swapLines(k, line1, line2);
        }
        transpositions++;

        if (getRes() != null) {
            if (k.isCoefs()) {
                stepMatrices.add(new Matrix(k.getValuesMap(), k.getCoefsMap(), k.getRowCount(), k.getColumnCount()));
            } else {
                stepMatrices.add(new Matrix(k.getValuesMap(), k.getRowCount(), k.getColumnCount()));
            }
            stepStrings.add(getRes().getString(R.string.swap, line1 + 1, line2 + 1));
        }
    }

    void doDiff(boolean flag, Matrix k, Matrix extens, int line1, int line2) {
        RationalNumber coef = k.getValuesMap().get(line1 * 1000 + line2)
                .multiply(k.getValuesMap().get(line1 * 1000 + line1).inverse());

        if (flag) {
            LinesOperations.diffLines(k, extens, line1, line2, coef);
            if (getRes() != null) {
                stepExtensionMatrices.add(new Matrix(extens.getValuesMap(), extens.getRowCount(), extens.getColumnCount()));
            }
        } else {
            LinesOperations.diffLines(k, line1, line2, coef);
        }
        if (getRes() != null) {
            if (k.isCoefs()) {
                stepMatrices.add(new Matrix(k.getValuesMap(), k.getCoefsMap(), k.getRowCount(), k.getColumnCount()));
            } else {
                stepMatrices.add(new Matrix(k.getValuesMap(), k.getRowCount(), k.getColumnCount()));
            }
            stepStrings.add(getRes().getString(R.string.diff, line2 + 1, line1 + 1, coef.toString()));
        }
    }

    void doMultiply(boolean flag, Matrix k, Matrix extens, int line) {
        RationalNumber coef = k.getValuesMap().get(line * 1000 + line).inverse();
        if (flag) {
            LinesOperations.multiplyLine(k, extens, coef, line);
            if (getRes() != null) {
                stepExtensionMatrices.add(new Matrix(extens.getValuesMap(),
                        extens.getRowCount(), extens.getColumnCount()));
            }
        } else {

            LinesOperations.multiplyLine(k, coef, line);
        }

        if (getRes() != null) {
            if (k.isCoefs()) {
                stepMatrices.add(new Matrix(k.getValuesMap(), k.getCoefsMap(), k.getRowCount(), k.getColumnCount()));
            } else {
                stepMatrices.add(new Matrix(k.getValuesMap(), k.getRowCount(), k.getColumnCount()));
            }
            getStepStrings().add(getRes().getString(R.string.multi, line + 1, coef.toString()));
        }
    }

    public Matrix getResult() {
        return result;
    }

    int getTranspositions() {
        return transpositions;
    }
}
