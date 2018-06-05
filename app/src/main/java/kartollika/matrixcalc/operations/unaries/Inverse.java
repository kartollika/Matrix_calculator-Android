package kartollika.matrixcalc.operations.unaries;

import android.content.res.Resources;

import kartollika.matrixcalc.Matrix;
import kartollika.matrixcalc.R;
import kartollika.matrixcalc.operations.SteppableOperationUnary;

public class Inverse extends SteppableOperationUnary {

    public Inverse(Matrix m, Resources resources) {
        super(m, resources);
    }

    @Override
    public Matrix calc() {
        Matrix extens = new Matrix(null, getM().getRowCount(), getM().getColumnCount());
        extens.setEFrom0();

        ToE goToE = new ToE(getM(), extens, getRes());
        goToE.calc();
        Matrix result = goToE.getExtens();

        stepStrings.addAll(goToE.getStepStrings());
        stepStrings.add(getRes().getString(R.string.inverse_result));
        stepStrings.set(0, getRes().getString(R.string.e_start));

        stepMatrices.addAll(goToE.getStepMatrices());
        stepMatrices.add(new Matrix(result.getValuesMap(), result.getRowCount(), result.getColumnCount()));

        stepExtensionMatrices.addAll(goToE.getStepExtensionMatrices());

        return result;
    }
}
