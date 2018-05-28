package kartollika.matrixcalc.operations;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import kartollika.matrixcalc.Matrix;

/**
 * Created by kartollikaa on 27.05.2018.
 */

public abstract class SteppableOperationUnary extends UO implements Stepable {
    protected List<String> stepStrings = null;
    protected List<Matrix> stepMatrices = null;
    protected List<Matrix> stepExtensionMatrices = null;

    SteppableOperationUnary(Matrix m) {
        super(m);
    }

    protected SteppableOperationUnary(Matrix m, Matrix extens, Resources resources) {
        super(m, extens, resources);
        initStepArrays();
    }

    protected SteppableOperationUnary(Matrix m, int n, int which, Resources resources) {
        super(m, n, which, resources);
        initStepArrays();
    }

    protected SteppableOperationUnary(Matrix m, Resources resources) {
        super(m, resources);
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
