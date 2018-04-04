package kartollika.matrixcalc.unaryoperations;

import android.content.res.Resources;

import kartollika.matrixcalc.Matrix;
import kartollika.matrixcalc.Operation;

abstract class UO extends Operation {
    private Resources resources;
    private Matrix m;
    private int which;
    private int n;
    private Matrix extens;

    UO(Matrix m) {
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

    UO(Matrix m, Resources resources) {
        this.m = m;
        this.resources = resources;
    }

    Resources getRes() {
        return resources;
    }

    Matrix getM() {
        return m;
    }

    int getWhich() {
        return which;
    }

    int getN() {
        return n;
    }

    Matrix getExtens() {
        return extens;
    }
}
