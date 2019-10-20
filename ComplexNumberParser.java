package range;

import java.math.BigDecimal;

class ComplexNumberParser {

    static ComplexNumber parseComplexNumber(String coefficient) {
        //example 3 + 5i;
        String fullComplexNumberRegex = "-?[0-9]+(\\.[0-9]+)?[+-][0-9]*(\\.[0-9]*)?i";
//        String fullComplexNumberRegex = "-?[0-9]*(\\.[0-9]+)?[\\+-][0-9]*(\\.[0-9]*)?i";
        String realPartRegex = "-?[0-9]+(\\.[0-9]+)?";
        //-5.5i
        String imaginaryPartRegex = "[+-]?[0-9]*(\\.[0-9]*)?i";

        if (coefficient.matches("0")) {
            return new ComplexNumber(BigDecimal.valueOf(0), BigDecimal.valueOf(0));
        } else if (coefficient.matches(fullComplexNumberRegex)) {

            String realPart = coefficient.split(imaginaryPartRegex)[0];
            String imaginaryPart = coefficient.split(realPart + "\\+?", 2)[1];
            imaginaryPart = editImaginaryPart(imaginaryPart);
            return new ComplexNumber(new BigDecimal(realPart), new BigDecimal(imaginaryPart));
            // if we get only imaginary part
        } else if (coefficient.matches(imaginaryPartRegex)) {
            String imaginaryPart = editImaginaryPart(coefficient);
            return new ComplexNumber(BigDecimal.valueOf(0), new BigDecimal(imaginaryPart));
            // if we get only real part, simple make ComplexNumber object
        } else {
            return new ComplexNumber(new BigDecimal(coefficient), BigDecimal.valueOf(0));
        }

    }

    private static String editImaginaryPart(String imaginaryPart) {
        if (imaginaryPart.length() == 1) {
            return imaginaryPart = "1";
            //-i
        } else if (imaginaryPart.length() == 2 && imaginaryPart.charAt(0) == '-') {
            return imaginaryPart = "-1";
            //-123.1234i
        } else {
            return imaginaryPart = imaginaryPart.split("i")[0];
        }
    }
}
