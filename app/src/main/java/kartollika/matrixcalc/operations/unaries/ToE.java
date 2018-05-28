package kartollika.matrixcalc.operations.unaries;

import android.content.res.Resources;

import kartollika.matrixcalc.Matrix;
import kartollika.matrixcalc.RationalNumber;

public class ToE extends ToDiagonal {

    ToE(Matrix m, Matrix extens, Resources resources) {
        super(m, extens, resources);
    }

    @Override
    public Matrix calc() {
        super.calc();

        boolean flag = false;
        Matrix result = super.getResult();
        Matrix extens = super.getExtens();

        if (extens != null) {
            flag = true;
        }

        int iter = 0;
        while (iter < result.getRowCount()) {
            RationalNumber elem;
            try {
                elem = result.getValuesMap().get(iter * 1000 + iter);
            } catch (Exception e) {
                break;
            }
            if (elem == null) {
                return extens;
            }

            if (!elem.equals(RationalNumber.ZERO)) {
                if (!elem.equals(RationalNumber.ONE)) {
                    doMultiply(flag, result, extens, iter);
                }
                for (int i = iter - 1; i >= 0; --i) {
                    if (!result.getValuesMap().get(iter * 1000 + i).equals(RationalNumber.ZERO)) {
                        doDiff(flag, result, extens, iter, i);
                    }
                }
            } else {
                ++iter;
                continue;
            }
            ++iter;
        }
        return extens;
    }
}
