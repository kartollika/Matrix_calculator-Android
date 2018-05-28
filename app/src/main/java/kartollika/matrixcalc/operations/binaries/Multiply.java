package kartollika.matrixcalc.operations.binaries;

import android.util.SparseArray;

import kartollika.matrixcalc.Matrix;
import kartollika.matrixcalc.RationalNumber;
import kartollika.matrixcalc.operations.BO;

public class Multiply extends BO {
    public Multiply(Matrix m1, Matrix m2) {
        super(m1, m2);
    }

    @Override
    public Matrix calc() {
        Matrix a = getM1();
        Matrix b = getM2();
        SparseArray<RationalNumber> valuesMap = new SparseArray<>();

        RationalNumber elem = new RationalNumber(0, 1);

        for (int i = 0; i < b.getColumnCount(); ++i) {
            for (int j = 0; j < a.getRowCount(); ++j) {
                for (int k = 0; k < a.getColumnCount(); ++k) {
                    elem = elem.sum(a.getValuesMap().get(k * 1000 + j).multiply(b.getValuesMap().get(i * 1000 + k)));
                }
                valuesMap.put(i * 1000 + j, elem);
                elem = new RationalNumber(0, 1);
            }
        }

        return new Matrix(valuesMap, a.getRowCount(), b.getColumnCount());
    }


}
