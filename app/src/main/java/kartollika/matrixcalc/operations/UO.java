package kartollika.matrixcalc.operations;

import android.content.res.Resources;

import kartollika.matrixcalc.Matrix;

public abstract class UO extends Operation {
    private Resources resources;
    private Matrix m;
    private int which;
    private int n;
    private Matrix extens;

    protected UO(Matrix m) {
        this.m = m;
    }

    UO(Matrix m, Matrix extens, Resources resources) {
        this.m = m;
        this.extens = extens;
        this.resources = resources;
    }

    UO(Matrix m, int n, int which, Resources resources) {
        this.m = m;
        this.n = n;
        this.which = which;
        this.resources = resources;
    }

    protected UO(Matrix m, Resources resources) {
        this.m = m;
        this.resources = resources;
    }

    protected Resources getRes() {
        return resources;
    }

    protected Matrix getM() {
        return m;
    }

    protected int getWhich() {
        return which;
    }

    protected int getN() {
        return n;
    }

    public Matrix getExtens() {
        return extens;
    }
}
