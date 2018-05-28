package kartollika.matrixcalc.operations;

import java.util.List;

import kartollika.matrixcalc.Matrix;

/**
 * Created by kartollikaa on 27.05.2018.
 */

public interface Stepable {

    List<String> getStepStrings();

    List<Matrix> getStepMatrices();

    List<Matrix> getStepExtensionMatrices();
}
