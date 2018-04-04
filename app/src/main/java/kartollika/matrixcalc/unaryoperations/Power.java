package kartollika.matrixcalc.unaryoperations;

import android.content.res.Resources;

import java.util.ArrayList;

import kartollika.matrixcalc.Matrix;
import kartollika.matrixcalc.R;
import kartollika.matrixcalc.binaryoperations.Multiply;

public class Power extends UO {

    private Object[] objects;

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
        ArrayList<Matrix> matrices = new ArrayList<>();
        ArrayList<String> strings = new ArrayList<>();

        def.append(getRes().getString(R.string.pow_start));
        def.append(n).append(" = ");
        strings.add(String.valueOf(def));

        matrices.add(step);

        int iter = 1;
        while (n > 0) {
            StringBuilder s = new StringBuilder("");
            boolean flag = false;

            if ((n & 1) == 1) {
                result = new Multiply(result, step).calc();
                flag = true;
                def.append(iter).append(" + ");
            }

            if (flag) {
                s.append(getRes().getString(R.string.needed_step_pow));
            } else {
                s.append(getRes().getString(R.string.step_pow));
            }

            s.append((getWhich() == 0) ? "A" : "B");
            s.append("<sup>").append(iter).append("</sup>:");

            matrices.add(step);
            step = new Multiply(step, step).calc();

            strings.add(String.valueOf(s));

            iter *= 2;
            n >>= 1;
        }

        def.delete(def.length() - 3, def.length());
        strings.set(0, String.valueOf(def));

        strings.add(getRes().getString(R.string.pow_result));
        matrices.add(result);

        objects = new Object[2];
        objects[0] = matrices;
        objects[1] = strings;

        return result;
    }

    public Object[] getObjects() {
        return objects;
    }
}
