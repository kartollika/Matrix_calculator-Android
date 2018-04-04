package kartollika.matrixcalc.binaryoperations;

import kartollika.matrixcalc.Matrix;
import kartollika.matrixcalc.Operation;
import kartollika.matrixcalc.RationalNumber;

abstract class BO extends Operation {

    private Matrix m1, m2;
    private RationalNumber k;

    BO(Matrix m1, Matrix m2) {
        this.m1 = m1;
        this.m2 = m2;
    }

    BO(Matrix m1, RationalNumber k) {
        this.m1 = m1;
        this.k = k;
    }

    Matrix getM1() {
        return m1;
    }

    Matrix getM2() {
        return m2;
    }

    RationalNumber getK() {
        return k;
    }
}
