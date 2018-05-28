package kartollika.matrixcalc;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

public class Matrix implements Parcelable {
    boolean coefs;
    private int dimN;
    private int dimM;

    private SparseArray<RationalNumber> valuesMap;
    private SparseArray<RationalNumber> coefsMap;

    private boolean isEdited;

    public Matrix(SparseArray<RationalNumber> valuesMap, int dimN, int dimM) {
        setRowCount(dimN);
        setColumnCount(dimM);

        if (valuesMap == null) {
            this.valuesMap = new SparseArray<>();
            setDefault();
        } else {
            setValuesMap(valuesMap);
        }
    }

    public Matrix(Matrix another) {
        dimN = another.dimN;
        dimM = another.dimM;
        valuesMap = another.valuesMap;
        coefsMap = another.coefsMap;
        coefs = another.coefs;
        isEdited = another.isEdited;
    }

    public Matrix(SparseArray<RationalNumber> valuesMap, SparseArray<RationalNumber> coefsMap, int dimN, int dimM) {
        setRowCount(dimN);
        setColumnCount(dimM);

        if (valuesMap == null) {
            setDefault();
        } else {
            setValuesMap(valuesMap);
        }

        if (coefsMap == null) {
            setDefaultAug();
        } else {
            setCoefsMap(coefsMap);
        }

        coefs = true;
    }

    private void setDefault() {
        SparseArray<RationalNumber> valuesMapDef = new SparseArray<>();

        for (int i = 0; i < getColumnCount(); ++i) {
            for (int j = 0; j < getRowCount(); ++j) {
                valuesMapDef.put(i * 1000 + j, RationalNumber.ZERO);
            }
        }
        setValuesMap(valuesMapDef);
    }

    public void setEFrom0() {
        for (int i = 0; i < getColumnCount(); ++i) {
            getValuesMap().put(i * 1000 + i, RationalNumber.ONE);
        }
    }

    void setRowCount(final int dimN) {
        this.dimN = dimN;
    }

    void setColumnCount(final int dimM) {
        this.dimM = dimM;
    }

    void setValuesMap(SparseArray<RationalNumber> valuesMap) {
        this.valuesMap = new SparseArray<>();
        for (int i = 0; i < getColumnCount(); ++i) {
            for (int j = 0; j < getRowCount(); ++j) {
                this.valuesMap.put(i * 1000 + j, valuesMap.get(i * 1000 + j));
            }
        }
    }

    public int getRowCount() {
        return dimN;
    }

    public int getColumnCount() {
        return dimM;
    }

    public SparseArray<RationalNumber> getValuesMap() {
        return valuesMap;
    }

    private void setDefaultAug() {
        SparseArray<RationalNumber> coefsMapDef = new SparseArray<>();
        for (int i = 0; i < getRowCount(); ++i) {
            coefsMapDef.put(100_000 + i, RationalNumber.ZERO);
        }
        setCoefsMap(coefsMapDef);
    }

    public SparseArray<RationalNumber> getCoefsMap() {
        return coefsMap;
    }

    void setCoefsMap(SparseArray<RationalNumber> coefsMap) {
        this.coefsMap = new SparseArray<>();
        for (int i = 0; i < getRowCount(); ++i) {
            this.coefsMap.put(100_000 + i, coefsMap.get(100_000 + i));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;

        Matrix m = (Matrix) obj;

        if (this.getRowCount() != m.getRowCount() || this.getColumnCount() != m.getColumnCount()) {
            return false;
        }

        for (int i = 0; i < this.getColumnCount(); ++i) {
            for (int j = 0; j < this.getRowCount(); ++j) {
                if (!(this.getValuesMap().get(i * 1000 + j)).equals(m.getValuesMap().get(i * 1000 + j))) {
                    return false;
                }
            }
        }

        for (int i = 0; i < this.getRowCount(); ++i) {
            if (!(this.getCoefsMap().get(100_000 + i)).equals(m.getCoefsMap().get(100_000 + i))) {
                return false;
            }
        }
        return true;
    }

    public void setEdited() {
        isEdited = true;
    }

    public boolean isEdited() {
        return isEdited;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected Matrix(Parcel in) {
        dimN = in.readInt();
        dimM = in.readInt();
        coefs = in.readInt() != 0;

        valuesMap = new SparseArray<>();

        for (int i = 0; i < dimM; ++i) {
            for (int j = 0; j < dimN; ++j) {
                valuesMap.put(i * 1000 + j, (RationalNumber) in.readValue(RationalNumber.class.getClassLoader()));
            }
        }
        if (coefs) {
            coefsMap = new SparseArray<>();
            for (int i = 0; i < dimN; ++i) {
                coefsMap.put(100_000 + i, (RationalNumber) in.readValue(RationalNumber.class.getClassLoader()));
            }
        }
        isEdited = in.readInt() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dimN);
        dest.writeInt(dimM);
        dest.writeByte((byte) (coefs ? 1 : 0));

        for (int i = 0; i < dimM; ++i) {
            for (int j = 0; j < dimN; ++j) {
                dest.writeValue(valuesMap.get(i * 1000 + j));
            }
        }

        if (coefsMap != null) {
            for (int i = 0; i < dimN; ++i) {
                dest.writeValue(coefsMap.get(100_000 + i));
            }
        }
        dest.writeInt(isEdited ? 1 : 0);
    }

    public static final Creator<Matrix> CREATOR = new Creator<Matrix>() {
        @Override
        public Matrix createFromParcel(Parcel in) {
            return new Matrix(in);
        }

        @Override
        public Matrix[] newArray(int size) {
            return new Matrix[size];
        }
    };

    public void setCoefs(boolean coefs) {
        this.coefs = coefs;
    }

    public boolean isCoefs() {
        return coefs;
    }
}

