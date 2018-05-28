package kartollika.matrixcalc.operations.unaries;

import android.content.res.Resources;
import android.util.SparseArray;

import kartollika.matrixcalc.Matrix;
import kartollika.matrixcalc.R;
import kartollika.matrixcalc.RationalNumber;
import kartollika.matrixcalc.operations.SteppableOperationUnary;

public class Determinant extends SteppableOperationUnary {

    private Matrix result;
    private RationalNumber resultRational;

    public Determinant(Matrix m, Resources resources) {
        super(m, resources);
    }

    @Override
    public Matrix calc() {
        RationalNumber det = new RationalNumber(1, 1);

        ToDiagonal goToDiagonal = new ToDiagonal(getM(), null, getRes());
        Matrix m = goToDiagonal.calc();

        stepStrings.addAll(goToDiagonal.getStepStrings());
        stepMatrices.addAll(goToDiagonal.getStepMatrices());

        if (stepMatrices.size() > 0) {
            stepStrings.add(getRes().getString(R.string.mult_diag) + "<sup><xliff:g example=\"0\" id=\"pow\">" + goToDiagonal.getTranspositions() + "</xliff:g></sup> - " + getRes().getString(R.string.trans_sign));
            stepStrings.set(0, getRes().getString(R.string.triangle_start));
            stepMatrices.add(new Matrix(m.getValuesMap(), m.getRowCount(), m.getColumnCount()));
        }

        for (int i = 0; i < m.getColumnCount(); ++i) {
            det = det.multiply(m.getValuesMap().get(i * 1000 + i));
            if (det.equals(RationalNumber.ZERO)) {
                break;
            }
        }

        if (goToDiagonal.getTranspositions() % 2 == 1) {
            det = det.changeSign();
        }

        SparseArray<RationalNumber> sparseArray = new SparseArray<>();
        sparseArray.put(0, det);
        result = new Matrix(sparseArray, 1, 1);

        if (stepMatrices.size() > 0) {
            stepMatrices.add(result);
            stepStrings.add(getRes().getString(R.string.determinant_result));
        }

        resultRational = det;
        return result;
    }

    public Matrix getResult() {
        return result;
    }

    public RationalNumber getResultRational() {
        return resultRational;
    }
}
