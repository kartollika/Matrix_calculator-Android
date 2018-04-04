package kartollika.matrixcalc.unaryoperations;

import android.content.res.Resources;

import java.util.ArrayList;

import kartollika.matrixcalc.Matrix;
import kartollika.matrixcalc.R;

public class Inverse extends UO {
    private Object[] objects;

    public Inverse(Matrix m, Resources resources) {
        super(m, resources);
    }

    @Override
    protected Matrix calc() {
        Matrix extens = new Matrix(null, getM().getRowCount(), getM().getColumnCount());
        extens.setEFrom0();

        ToE goToE = new ToE(getM(), extens, getRes());
        goToE.calc();
        Matrix result = goToE.getExtens();

        ArrayList<String> strings = goToE.getStrings();
        ArrayList<Matrix> matrices = goToE.getMatrices();

        strings.add(getRes().getString(R.string.inverse_result));
        strings.set(0, getRes().getString(R.string.e_start));
        matrices.add(new Matrix(result.getValuesMap(), result.getRowCount(), result.getColumnCount()));

        objects = new Object[3];
        objects[0] = matrices;
        objects[1] = strings;
        objects[2] = goToE.getExtensMatrices();

        return result;
    }

    public Object[] getObjects() {
        return objects;
    }
}
