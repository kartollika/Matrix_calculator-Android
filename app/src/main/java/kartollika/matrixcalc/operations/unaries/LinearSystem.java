package kartollika.matrixcalc.operations.unaries;

import android.content.res.Resources;

import kartollika.matrixcalc.Matrix;
import kartollika.matrixcalc.R;
import kartollika.matrixcalc.RationalNumber;
import kartollika.matrixcalc.operations.SteppableOperationUnary;

public class LinearSystem extends SteppableOperationUnary {

    public LinearSystem(Matrix m, Resources resources) {
        super(m, resources);
    }

    @Override
    protected Matrix calc() {
        int countZeros;
        int rank;
        int vars;

        ToE goToE = new ToE(getM(), null, getRes());
        goToE.calc();
        Matrix result = goToE.getResult();
        vars = result.getColumnCount();

        stepMatrices.addAll(goToE.getStepMatrices());
        stepStrings.addAll(goToE.getStepStrings());
        stepStrings.set(0, getRes().getString(R.string.gauss_start));

        StringBuilder stringBuilder = new StringBuilder(getRes().getString(R.string.hasSolves)).append("<br>");
        int max_rank = Math.min(result.getRowCount(), result.getColumnCount());
        StringBuilder[] stringBuilders = new StringBuilder[vars];
        for (int i = 0; i < vars; ++i) {
            stringBuilders[i] = new StringBuilder("");
        }

        rank = max_rank;

        for (int i = result.getRowCount() - 1; i >= 0; --i) {
            countZeros = 0;
            for (int j = 0; j < result.getColumnCount(); j++) {
                RationalNumber elem = result.getValuesMap().get(1000 * j + i);
                if (elem.equals(RationalNumber.ZERO)) {
                    countZeros++;
                    continue;
                }
                if (stringBuilders[i].length() == 0) {
                    if (elem.sign() == 1) {
                        stringBuilders[i].append(getElemString(elem, j));
                    } else {
                        stringBuilders[i].append("- ").append(getElemString(elem.changeSign(), j));
                    }
                } else {
                    if (elem.sign() == 1) {
                        stringBuilders[i].append(" + ").append(getElemString(elem, j));
                    } else {
                        stringBuilders[i].append(" - ").append(getElemString(elem.changeSign(), j));
                    }

                }
            }

            if (countZeros == result.getColumnCount()) {
                if (!result.getCoefsMap().get(100_000 + i).equals(RationalNumber.ZERO)) {
                    stepStrings.add(getRes().getString(R.string.noSolves, i + 1, result.getCoefsMap().get(100_000 + i)));
                    stepMatrices.add(new Matrix(result.getValuesMap(), result.getCoefsMap(),
                            result.getRowCount(), result.getColumnCount()));

                    return result;
                }
                rank--;
            } else {
                stringBuilders[i].append(" = ").append(result.getCoefsMap().get(100_000 + i));
            }
        }
        int cur_rank = rank;
        rank = max_rank;

        while (rank < vars) {
            //stringBuilders[rank].append("x<sub>").append(rank + 1).append("</sub> - любое");
            rank++;
        }

        if (cur_rank > 4) {
            stringBuilder.append(" <i><small>(").append(getRes().getString(R.string.scroll_to_see_more)).append(")</small></i><br>");
        }

        int i = 0;
        int q = 0;
        while (!stringBuilders[i].toString().equals("")) {
            ++q;
            if (++i == stringBuilders.length)
                break;
        }
        for (int j = 0; j < q; ++j) {
            if (j < q - 1) {
                stringBuilder.append(stringBuilders[j].append("<br>"));
            } else {
                stringBuilder.append(stringBuilders[j]);
            }
        }

        stepStrings.add(stringBuilder.toString());
        stepMatrices.add(new Matrix(result.getValuesMap(), result.getCoefsMap(),
                result.getRowCount(), result.getColumnCount()));
        return result;
    }

    private String getElemString(RationalNumber elem, int j) {
        String s = "";

        if (!elem.equals(RationalNumber.ONE)) {
            s += elem.toString() + "x<sub>" + String.valueOf(j + 1) + "</sub>";
        } else {
            s += "x<sub>" + String.valueOf(j + 1) + "</sub>";
        }
        return s;
    }

}
