package kartollika.matrixcalc.operations.unaries;

import kartollika.matrixcalc.Matrix;
import kartollika.matrixcalc.RationalNumber;

class LinesOperations {

    static void swapLines(Matrix m, int line1, int line2) {
        for (int i = 0; i < m.getColumnCount(); ++i) {
            RationalNumber buf = m.getValuesMap().get(i * 1000 + line1);
            m.getValuesMap().put(i * 1000 + line1, m.getValuesMap().get(i * 1000 + line2));
            m.getValuesMap().put(i * 1000 + line2, buf);
        }

        RationalNumber buf = m.getCoefsMap().get(100_000 + line1);
        m.getCoefsMap().put(100_000 + line1, m.getCoefsMap().get(100_000 + line2));
        m.getCoefsMap().put(100_000 + line2, buf);
    }

    static void swapLines(Matrix m, Matrix extens, int line1, int line2) {
        swapLines(m, line1, line2);

        for (int i = 0; i < m.getColumnCount(); ++i) {
            RationalNumber buf = extens.getValuesMap().get(i * 1000 + line1);
            extens.getValuesMap().put(i * 1000 + line1, extens.getValuesMap().get(i * 1000 + line2));
            extens.getValuesMap().put(i * 1000 + line2, buf);
        }
    }

    static void diffLines(Matrix m, int lineWhat, int lineFrom, RationalNumber coef) {
        for (int i = 0; i < m.getColumnCount(); ++i) {
            m.getValuesMap().put(i * 1000 + lineFrom,
                    m.getValuesMap().get(i * 1000 + lineFrom)
                            .sum(m.getValuesMap().get(i * 1000 + lineWhat).multiply(coef).changeSign()));
        }

        m.getCoefsMap().put(100_000 + lineFrom,
                m.getCoefsMap().get(100_000 + lineFrom).sum(m.getCoefsMap().get(100_000 + lineWhat).multiply(coef).changeSign()));
    }

    static void diffLines(Matrix m, Matrix extens, int lineWhat, int lineFrom, RationalNumber coef) {
        diffLines(m, lineWhat, lineFrom, coef);

        for (int i = 0; i < extens.getColumnCount(); ++i) {
            extens.getValuesMap().put(i * 1000 + lineFrom,
                    extens.getValuesMap().get(i * 1000 + lineFrom)
                            .sum(extens.getValuesMap().get(i * 1000 + lineWhat).multiply(coef).changeSign()));
        }
    }

    static void multiplyLine(Matrix m, RationalNumber coef, int line) {
        for (int i = 0; i < m.getColumnCount(); ++i) {
            m.getValuesMap().put(i * 1000 + line,
                    m.getValuesMap().get(i * 1000 + line).multiply(coef));

        }

        m.getCoefsMap().put(100_000 + line, m.getCoefsMap().get(100_000 + line).multiply(coef));
    }


    static void multiplyLine(Matrix m, Matrix extens, RationalNumber coef, int line) {
        multiplyLine(m, coef, line);

        for (int i = 0; i < extens.getColumnCount(); ++i) {
            extens.getValuesMap().put(i * 1000 + line,
                    extens.getValuesMap().get(i * 1000 + line).multiply(coef));
        }
    }
}
