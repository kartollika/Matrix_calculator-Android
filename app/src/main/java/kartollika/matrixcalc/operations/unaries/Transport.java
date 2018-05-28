package kartollika.matrixcalc.operations.unaries;

import android.util.SparseArray;

import kartollika.matrixcalc.Matrix;
import kartollika.matrixcalc.RationalNumber;
import kartollika.matrixcalc.operations.UO;

public class Transport extends UO {

    public Transport(Matrix m) {
        super(m);
    }

    @Override
    public Matrix calc() {
        Matrix a = getM();
        SparseArray<RationalNumber> valuesMap = a.getValuesMap();
        SparseArray<RationalNumber> newValuesMap = new SparseArray<>();

        for (int i = 0; i < a.getRowCount(); ++i) {
            for (int j = 0; j < a.getColumnCount(); ++j) {
                newValuesMap.put(i * 1000 + j, valuesMap.get(j * 1000 + i));
            }
        }
        return new Matrix(newValuesMap, a.getColumnCount(), a.getRowCount());
    }
}
