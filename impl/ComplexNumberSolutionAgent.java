package range.impl;

import range.ComplexNumber;
import range.LinearEquation;
import range.Row;
import range.SolutionAgent;

import java.math.BigDecimal;
import java.util.ArrayList;

class ComplexNumberSolutionAgent implements SolutionAgent {


    @Override
    public void findSolution(ArrayList<Row> system, String fileName) {
        ArrayList<ComplexNumber> solution = getSolution(system);
        new SolutionFileWriterImpl().printSolution(solution, fileName);
    }

    private ArrayList<ComplexNumber> getSolution(ArrayList<Row> system) {
        ArrayList<ComplexNumber> solution = new ArrayList<>();
        for (int i = 0; i < LinearEquation.numberOfCoefficients - 1; i++) {
            ComplexNumber constant = (ComplexNumber) system.get(i).getConstant();
            ComplexNumber coefficient = (ComplexNumber) system.get(i).getCoefficientByPosition(i);
            ComplexNumber variable = constant.divide(coefficient);
            solution.add(variable);
        }
        return solution;
    }

    @Override
    public boolean isNoSolution(ArrayList<Row> system) {
        for (Row row : system) {
            int zeroCount = 0;
            for (int j = 0; j < LinearEquation.numberOfCoefficients - 1; j++) {
                ComplexNumber complexNumber = (ComplexNumber) row.getCoefficientByPosition(j);
                if (complexNumber.getImaginaryPart().equals(BigDecimal.valueOf(0))
                        && complexNumber.getRealPart().equals(BigDecimal.valueOf(0))) {
                    zeroCount++;
                }
            }
            ComplexNumber constant = (ComplexNumber) row.getConstant();
            if (zeroCount == LinearEquation.numberOfCoefficients - 1
                    && (!constant.getRealPart().equals(BigDecimal.valueOf(0))
                    && !constant.getImaginaryPart().equals(BigDecimal.valueOf(0)))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isInfinitelyManySolutions(ArrayList<Row> system) {
        for (int i = 0; i < LinearEquation.numberOfCoefficients - 1; i++) {
            for (int j = i; j < system.size() - 1; j++) {
                ComplexNumber complexNumber1 = (ComplexNumber) system.get(j + 1).getCoefficientByPosition(i);
                ComplexNumber complexNumber2 = (ComplexNumber) system.get(j).getCoefficientByPosition(i);
                ComplexNumber result = complexNumber1.divide(complexNumber2);
                if (!result.getImaginaryPart().equals(BigDecimal.valueOf(0))
                        || !result.getRealPart().equals(BigDecimal.valueOf(0))) {
                    return false;
                }
            }
        }
        int numberOfSignificantEquals = 0;
        for (Row row : system) {
            for (int j = 0; j < LinearEquation.numberOfCoefficients - 1; j++) {
                ComplexNumber complexNumber = (ComplexNumber) row.getCoefficientByPosition(j);
                if (!complexNumber.getRealPart().equals(BigDecimal.valueOf(0))
                        || !complexNumber.getImaginaryPart().equals(BigDecimal.valueOf(0))) {
                    numberOfSignificantEquals++;
                    break;
                }
            }
        }
        return numberOfSignificantEquals < LinearEquation.numberOfCoefficients - 1;
    }
}
