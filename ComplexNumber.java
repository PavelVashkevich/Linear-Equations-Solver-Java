package range;

import java.math.BigDecimal;
import java.math.MathContext;

public class ComplexNumber {

    private BigDecimal realPart;
    private BigDecimal imaginaryPart;

    ComplexNumber(BigDecimal realPart, BigDecimal imaginaryPart) {
        this.realPart = realPart.round(new MathContext(4));
        this.imaginaryPart = imaginaryPart.round(new MathContext(4));
    }

    public BigDecimal getRealPart() {
        return realPart;
    }

    public void setRealPart(BigDecimal realPart) {
        this.realPart = realPart;
    }

    public BigDecimal getImaginaryPart() {
        return imaginaryPart;
    }

    public void setImaginaryPart(BigDecimal imaginaryPart) {
        this.imaginaryPart = imaginaryPart;
    }

    public ComplexNumber add(ComplexNumber b) {
        ComplexNumber a = this;
        BigDecimal realPart = a.getRealPart().add(b.getRealPart());
        BigDecimal imaginaryPart = a.imaginaryPart.add(b.getImaginaryPart());

        return new ComplexNumber(realPart, imaginaryPart);
    }

    public ComplexNumber subtract(ComplexNumber b) {
        ComplexNumber a = this;
        BigDecimal realPart = a.realPart.subtract(b.getRealPart());
        BigDecimal imaginaryPart = a.imaginaryPart.subtract(b.getImaginaryPart());

        return new ComplexNumber(realPart, imaginaryPart);
    }

    public ComplexNumber multiply(ComplexNumber b) {
        ComplexNumber a = this;
        BigDecimal realPart = (a.realPart.multiply(b.getRealPart())).subtract(a.imaginaryPart.multiply(b.getImaginaryPart()));
        BigDecimal imaginaryPart = (a.realPart.multiply(b.getImaginaryPart())).add(a.imaginaryPart.multiply(b.getRealPart()));

        return new ComplexNumber(realPart, imaginaryPart);
    }

    private ComplexNumber reciprocal() {
        BigDecimal scale = (this.realPart.multiply(this.realPart)).add(this.imaginaryPart.multiply(this.imaginaryPart));
        if (scale.equals(BigDecimal.valueOf(0))) {
            return new ComplexNumber(BigDecimal.valueOf(0), BigDecimal.valueOf(0));
        }
        return new ComplexNumber(realPart.divide(scale, MathContext.DECIMAL32),
                (imaginaryPart.multiply(BigDecimal.valueOf(-1))).divide(scale, new MathContext(4)));
    }

    public ComplexNumber divide(ComplexNumber b) {
        return this.multiply(b.reciprocal());
    }

    @Override
    public String toString() {

        if (imaginaryPart.doubleValue() == 0) return realPart + "";
        if (imaginaryPart.doubleValue() == 1) {
            if (realPart.doubleValue() == 0) {
                return "i";
            } else {
                return realPart + "+" + "i";
            }
        }
        if (imaginaryPart.doubleValue() == -1) {
            if (realPart.doubleValue() == 0) {
                return "i";
            } else {
                return realPart + "-" + "i";
            }
        }
        if (realPart.doubleValue() == 0) return imaginaryPart + "i";
        if (imaginaryPart.doubleValue() < 0) return realPart + "" + imaginaryPart + "i";
        return realPart + "+" + imaginaryPart + "i";
    }
}
