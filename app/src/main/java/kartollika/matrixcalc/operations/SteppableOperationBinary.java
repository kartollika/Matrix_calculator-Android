package kartollika.matrixcalc.operations;

import java.util.ArrayList;
import java.util.List;

import kartollika.matrixcalc.Matrix;
import kartollika.matrixcalc.RationalNumber;

/**
 * Created by kartollikaa on 27.05.2018.
 */

abstract class SteppableOperationBinary extends BO implements Stepable {
    private List<String> stepStrings = null;
    private List<Matrix> stepMatrices = null;
    private List<Matrix> stepExtensionMatrices = null;

    SteppableOperationBinary(Matrix m1, Matrix m2) {
        super(m1, m2);
        initStepArrays();
    }

    SteppableOperationBinary(Matrix m1, RationalNumber k) {
        super(m1, k);
        initStepArrays();
    }

    private void initStepArrays() {
        stepStrings = new ArrayList<>();
        stepMatrices = new ArrayList<>();
        stepExtensionMatrices = new ArrayList<>();
    }

    @Override
    public List<String> getStepStrings() {
        return stepStrings;
    }

    @Override
    public List<Matrix> getStepMatrices() {
        return stepMatrices;
    }

    @Override
    public List<Matrix> getStepExtensionMatrices() {
        return stepExtensionMatrices;
    }
}
