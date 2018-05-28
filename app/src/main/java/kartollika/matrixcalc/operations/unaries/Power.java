package kartollika.matrixcalc.operations.unaries;

import android.content.res.Resources;

import kartollika.matrixcalc.Matrix;
import kartollika.matrixcalc.R;
import kartollika.matrixcalc.operations.SteppableOperationUnary;
import kartollika.matrixcalc.operations.binaries.Multiply;

public class Power extends SteppableOperationUnary {

    public Power(Matrix m, int n, int which, Resources resources) {
        super(m, n, which, resources);
    }

    @Override
    public Matrix calc() {
        int n = getN();
        Matrix step = getM();
        Matrix result = new Matrix(null, getM().getRowCount(), getM().getColumnCount());
        result.setEFrom0();

        StringBuilder def = new StringBuilder("");
        def.append(getRes().getString(R.string.pow_start));
        def.append(n).append(" = ");
        stepStrings.add(def.toString());
        stepMatrices.add(step);

        int iter = 1;
        while (n > 0) {
            StringBuilder stepBuilder = new StringBuilder("");
            boolean flag = false;

            if ((n & 1) == 1) {
                result = new Multiply(result, step).calc();
                flag = true;
                def.append(iter).append(" + ");
            }

            if (flag) {
                stepBuilder.append(getRes().getString(R.string.needed_step_pow));
            } else {
                stepBuilder.append(getRes().getString(R.string.step_pow));
            }

            stepBuilder.append((getWhich() == 0) ? "A" : "B");
            stepBuilder.append("<sup>").append(iter).append("</sup>:");

            stepMatrices.add(step);
            step = new Multiply(step, step).calc();
            stepStrings.add(stepBuilder.toString());

            iter *= 2;
            n >>= 1;
        }

        def.delete(def.length() - 3, def.length());
        stepStrings.set(0, def.toString());

        stepStrings.add(getRes().getString(R.string.pow_result));
        stepMatrices.add(result);

        return result;
    }
}
