package kartollika.matrixcalc.unaryoperations;

import android.content.res.Resources;

import java.util.ArrayList;

import kartollika.matrixcalc.Matrix;
import kartollika.matrixcalc.R;
import kartollika.matrixcalc.RationalNumber;

public class LinearSystem extends UO {
    private Object[] objects;

    public LinearSystem(Matrix m, Resources resources) {
        super(m, resources);
        objects = new Object[2];
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

        ArrayList<Matrix> matrices = goToE.getMatrices();
        ArrayList<String> strings = goToE.getStrings();
        strings.set(0, getRes().getString(R.string.gauss_start));

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
                    strings.add(getRes().getString(R.string.noSolves, i + 1, result.getCoefsMap().get(100_000 + i)));
                    matrices.add(new Matrix(result.getValuesMap(), result.getCoefsMap(),
                            result.getRowCount(), result.getColumnCount()));
                    objects[0] = matrices;
                    objects[1] = strings;
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
        while (!String.valueOf(stringBuilders[i]).equals("")) {
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
        /*for (int i = 0; i < stringBuilders.length; ++i) {
            if (!String.valueOf(stringBuilders[i]).equals("")) {
                if (i < stringBuilders.length - 2) {
                    stringBuilder.append(stringBuilders[i].append("<br>"));
                } else {
                    stringBuilder.append(stringBuilders[i]);
                }
            }
        }*/


        strings.add(String.valueOf(stringBuilder));
        matrices.add(new Matrix(result.getValuesMap(), result.getCoefsMap(),
                result.getRowCount(), result.getColumnCount()));
        objects[0] = matrices;
        objects[1] = strings;
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

    public Object[] getObjects() {
        return objects;
    }

}
