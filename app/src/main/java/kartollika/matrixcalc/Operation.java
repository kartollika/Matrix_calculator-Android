package kartollika.matrixcalc;

public abstract class Operation {
    private Matrix result;

    protected abstract Matrix calc();

    public Matrix getResult() {
        return result;
    }

    public void setResult(Matrix result) {
        this.result = result;
    }
}
