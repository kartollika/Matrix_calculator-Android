package kartollika.matrixcalc.unaryoperations;

import android.content.res.Resources;
import android.util.SparseArray;

import java.util.ArrayList;

import kartollika.matrixcalc.Matrix;
import kartollika.matrixcalc.R;
import kartollika.matrixcalc.RationalNumber;

public class Determinant extends UO {

    private Object[] objects;
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

        ArrayList<Matrix> matrices = goToDiagonal.getMatrices();
        ArrayList<String> strings = goToDiagonal.getStrings();

        if (matrices != null) {
            strings.add(getRes().getString(R.string.mult_diag) + "<sup><xliff:g example=\"0\" id=\"pow\">" + goToDiagonal.getTranspositions() + "</xliff:g></sup> - " + getRes().getString(R.string.trans_sign));
            strings.set(0, getRes().getString(R.string.triangle_start));
            matrices.add(new Matrix(m.getValuesMap(), m.getRowCount(), m.getColumnCount()));
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

        if (matrices != null) {
            matrices.add(result);
            strings.add(getRes().getString(R.string.determinant_result));

            objects = new Object[2];
            objects[0] = matrices;
            objects[1] = strings;
        }

        resultRational = det;
        return result;
    }

    public Object[] getObjects() {
        return objects;
    }

    public Matrix getResult() {
        return result;
    }

    public RationalNumber getResultRational() {
        return resultRational;
    }
}
