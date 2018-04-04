package kartollika.matrixcalc;

import java.io.Serializable;

public class RationalNumber implements Serializable {

    private static final RationalNumber NEGATIVE_INFINITY = new RationalNumber(-1, 0);
    private static final RationalNumber POSITIVE_INFINITY = new RationalNumber(1, 0);
    private static final RationalNumber NaN = new RationalNumber(0, 0);

    public static final RationalNumber ZERO = new RationalNumber(0, 1);
    public static final RationalNumber ONE = new RationalNumber(1, 1);

    private final long numerator;
    private final long denominator;

    public RationalNumber(long numerator, long denominator) {
        if (denominator == 1) {
            this.numerator = numerator;
            this.denominator = 1;
            return;
        }

        if (denominator < 0) {
            numerator = -numerator;
            denominator = -denominator;
        }

        // Convert to reduced form
        if (denominator == 0 && numerator > 0) {
            this.numerator = 1; // +Inf
            this.denominator = 0;
        } else if (denominator == 0 && numerator < 0) {
            this.numerator = -1; // -Inf
            this.denominator = 0;
        } else if (denominator == 0 && numerator == 0) {
            this.numerator = 0; // NaN
            this.denominator = 0;
        } else if (numerator == 0) {
            this.numerator = 0;
            this.denominator = 1;
        } else {
            long gcd = gcd(numerator, denominator);

            this.numerator = numerator / gcd;
            this.denominator = denominator / gcd;
        }
    }

    private static long gcd(long numerator, long denominator) {
        long a = numerator;
        long b = denominator;

        while (b != 0) {
            long oldB = b;

            b = a % b;
            a = oldB;
        }

        return Math.abs(a);
    }

    public double doubleValue() {
        return (double) numerator / (double) denominator;
    }

    private boolean isNaN() {
        return denominator == 0 && numerator == 0;
    }

    private boolean isFinite() {
        return denominator != 0;
    }

    public boolean isZero() {
        return isFinite() && numerator == 0;
    }

    public boolean isInfinite() {
        return numerator != 0 && denominator == 0;
    }

    private boolean isPosInf() {
        return denominator == 0 && numerator > 0;
    }

    private boolean isNegInf() {
        return denominator == 0 && numerator < 0;
    }

    public RationalNumber sum(RationalNumber another) {
        long thisNumerator = numerator * another.denominator;
        long anotherNumerator = another.numerator * denominator;
        long q = thisNumerator + anotherNumerator;

        return new RationalNumber(q, denominator * another.denominator);
    }

    public RationalNumber multiply(RationalNumber another) {

        long numeratorR = another.numerator * numerator;
        long denominatorR = another.denominator * denominator;

        return new RationalNumber(numeratorR, denominatorR);
    }

    public RationalNumber inverse() {
        if (numerator < 0) {
            return new RationalNumber(-denominator, -numerator);
        } else {
            return new RationalNumber(denominator, numerator);
        }
    }

    public long getNumerator() {
        return numerator;
    }

    public int sign() {
        return numerator > 0 ? 1 : 0;
    }

    public RationalNumber changeSign() {
        return new RationalNumber(-numerator, denominator);
    }

    public long getDenominator() {
        return denominator;
    }

    @Override
    public boolean equals(Object obj) {
        return (numerator == ((RationalNumber) obj).numerator && denominator == ((RationalNumber) obj).denominator);
    }

    public static RationalNumber abs(RationalNumber r) {
        return r.numerator >= 0 ? new RationalNumber(r.numerator, r.denominator)
                : new RationalNumber(-r.numerator, r.denominator);
    }

    public int compareTo(RationalNumber another) {

        if (equals(another)) {
            return 0;
        } else if (isNaN()) { // NaN is greater than the other non-NaN value
            return 1;
        } else if (another.isNaN()) { // the other NaN is greater than this non-NaN value
            return -1;
        } else if (isPosInf() || another.isNegInf()) {
            return 1; // positive infinity is greater than any non-NaN/non-posInf value
        } else if (isNegInf() || another.isPosInf()) {
            return -1; // negative infinity is less than any non-NaN/non-negInf value
        }

        // else both this and another are finite numbers

        // make the denominators the same, then compare numerators
        long thisNumerator = numerator * another.denominator; // long to avoid overflow
        long otherNumerator = another.numerator * denominator; // long to avoid overflow

        // avoid underflow from subtraction by doing comparisons
        if (thisNumerator < otherNumerator) {
            return -1;
        } else if (thisNumerator > otherNumerator) {
            return 1;
        } else {
            // This should be covered by #equals, but have this code path just in case
            return 0;
        }
    }

    @Override
    public String toString() {
        if (isNaN()) {
            return "NaN";
        } else if (isPosInf()) {
            return "Infinity";
        } else if (isNegInf()) {
            return "-Infinity";
        } else {
            if (numerator == 0) {
                return String.valueOf(0);
            }
            if (denominator == 1) {
                return String.valueOf(numerator);
            } else {
                return numerator + "/" + denominator;
            }
        }
    }

    public static RationalNumber parseRational(String string) throws NumberFormatException {

        if (string.length() == 0) {
            throw new NullPointerException();
        }

        switch (string) {
            case "NaN":
            case "Infinity":
            case "-Infinity":
                throw new NumberFormatException();
        }

        int sep_ix = string.indexOf('.');
        if (sep_ix < 0) {
            sep_ix = string.indexOf('/');
        } else {
            long q = (long) Math.pow(10, string.length() - sep_ix - 1);
            long num = Long.parseLong(string.substring(0, sep_ix) + string.substring(sep_ix + 1, string.length()));
            return new RationalNumber(num, q);
        }
        if (sep_ix < 0) {
            return new RationalNumber(Integer.parseInt(string), 1);
        }

        long q = Long.parseLong(string.substring(sep_ix + 1));
        if (q == 0) {
            throw new NumberFormatException();
        }
        return new RationalNumber(Integer.parseInt(string.substring(0, sep_ix)), q);
    }
}
