package kartollika.matrixcalc.binaryoperations;

import kartollika.matrixcalc.Matrix;
import kartollika.matrixcalc.RationalNumber;

public class ConstMultiply extends BO {
    public ConstMultiply(Matrix m1, RationalNumber k) {
        super(m1, k);
    }

    @Override
    public Matrix calc() {
        Matrix a = getM1();
        RationalNumber k = getK();
        Matrix result = new Matrix(null, a.getRowCount(), a.getColumnCount());

        for (int i = 0; i < result.getColumnCount(); ++i) {
            for (int j = 0; j < result.getRowCount(); ++j) {
                result.getValuesMap().put(i * 1000 + j, a.getValuesMap().get(i * 1000 + j).multiply(k));
            }
        }
        return result;
    }
}
