package kartollika.matrixcalc.operations.binaries;

import kartollika.matrixcalc.Matrix;
import kartollika.matrixcalc.operations.BO;

public class Sum extends BO {

    public Sum(Matrix m1, Matrix m2) {
        super(m1, m2);
    }

    @Override
    public Matrix calc() {
        Matrix a = getM1();
        Matrix b = getM2();
        Matrix result = new Matrix(null, a.getRowCount(), a.getColumnCount());

        for (int i = 0; i < result.getColumnCount(); ++i) {
            for (int j = 0; j < result.getRowCount(); ++j) {
                result.getValuesMap().put(i * 1000 + j, a.getValuesMap().get(i * 1000 + j).sum(b.getValuesMap().get(i * 1000 + j)));
            }
        }
        return result;
    }
}
