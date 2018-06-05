package kartollika.matrixcalc.operations;

import kartollika.matrixcalc.Matrix;

public abstract class Operation {
    private Matrix result;

    public abstract Matrix calc();

    public Matrix getResult() {
        return result;
    }

    public void setResult(Matrix result) {
        this.result = result;
    }
}
