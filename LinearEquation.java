package range;

import range.impl.ComplexNumberMatrix;
import range.impl.SimpleNumberMatrix;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class LinearEquation {
    private ArrayList<Row> rows = new ArrayList<Row>();
    public static int numberOfCoefficients;
    private boolean equationHaveComplexNumbers = false;

    LinearEquation(String fileName) {
        initLinearEquation(fileName);
    }

    private void initLinearEquation(String fileName) {
        boolean numberOfCoefficientsKnown = false;
        String[] equations = readFileAndGetEquationSystem(fileName);
        for (int i = 0; i < equations.length; i++) {
            Row row = new Row("R" + i);
            String[] coefficients = equations[i].split("\\s+");
            for (String coefficient : coefficients) {
                if (equationHaveComplexNumbers) {
                    ComplexNumber complexNumber = ComplexNumberParser.parseComplexNumber(coefficient);
                    row.addCoefficient(complexNumber);
                } else {
                    double doubleCoefficient = Double.parseDouble(coefficient);
                    row.addCoefficient(doubleCoefficient);
                }
            }
            rows.add(row);
            if (!numberOfCoefficientsKnown) {
                numberOfCoefficients = coefficients.length;
                numberOfCoefficientsKnown = true;
            }

        }
    }

    private String[] readFileAndGetEquationSystem(String fileName) {
        String[] equations = null;
        try {
            String linearEquationSystem = Files.readString(Path.of(fileName))
                    .replaceAll("\r\n", "<new>");

            String complexNumberRegex = ".*i.*";

            equations = linearEquationSystem.split("<new>");


            if (linearEquationSystem.matches(complexNumberRegex)) {
                equationHaveComplexNumbers = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return equations;
    }

    private Matrix factory() {
        if (equationHaveComplexNumbers) {
            return new ComplexNumberMatrix(rows);
        } else {
            return new SimpleNumberMatrix(rows);
        }
    }

    void solve(String outFile) {
        Matrix matrix = factory();
        printMatrix();
        matrix.solve(outFile);
    }

    private void printMatrix() {
        for (Row row : rows) {
            for (int j = 0; j < row.getNumberOfCoefficients(); j++) {
                System.out.print(row.getCoefficientByPosition(j) + " ");
            }
            System.out.println();
        }
    }

}
