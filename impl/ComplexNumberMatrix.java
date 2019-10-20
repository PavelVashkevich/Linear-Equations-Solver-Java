package range.impl;

import range.ComplexNumber;
import range.LinearEquation;
import range.Matrix;
import range.Row;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ComplexNumberMatrix implements Matrix {
    private ArrayList<Row> system;
    private SolutionContext solutionContext;
    private ArrayList<SwitchedColumn> columnsSwap;

    public ComplexNumberMatrix(ArrayList<Row> system) {
        this.system = system;
        solutionContext = new SolutionContext();
        solutionContext.setMethod(new ComplexNumberSolutionAgent());
        columnsSwap = new ArrayList<>();
    }

    @Override
    public void solve(String outFile) {
        System.out.println("Start solving the equation.");
        boolean noSolution = solutionContext.checkNoSolutionVariants(system, outFile);
        if (noSolution) {
            return;
        }
        System.out.println("Rows manipulation:");
        boolean make = makeAnotherRowsCoefficientsNull(outFile);
        if (!make) {
            return;
        }

        makeForm();
        if (columnsSwap.size() != 0) {
            returnColumnToItPlace();
        }
        solutionContext.findSolution(system, outFile);

    }

    private boolean makeAnotherRowsCoefficientsNull(String outFile) {

        for (int i = 0; i < LinearEquation.numberOfCoefficients - 1; i++) {
            ComplexNumber complexNumber = (ComplexNumber) system.get(i).getCoefficientByPosition(i);
            if (complexNumber.getImaginaryPart().equals(BigDecimal.valueOf(0))
                    && complexNumber.getRealPart().equals(BigDecimal.valueOf(0))) {
                searchNonZeroElement(i, i);
            } else if (!(complexNumber.getImaginaryPart().equals(BigDecimal.valueOf(1))
                    && complexNumber.getRealPart().equals(BigDecimal.valueOf(1)))) {
                divideRow(system.get(i), complexNumber);
            }

            for (int j = i + 1; j < system.size(); j++) {
                ComplexNumber complexNumber1 = (ComplexNumber) system.get(j).getCoefficientByPosition(i);
                ComplexNumber complexNumber2 = (ComplexNumber) system.get(i).getCoefficientByPosition(i);
                ComplexNumber multiplier = complexNumber1.divide(complexNumber2);
                if (!multiplier.getRealPart().equals(BigDecimal.valueOf(0))
                        || !multiplier.getImaginaryPart().equals(BigDecimal.valueOf(0))) {
                    subtractRows(system.get(j), system.get(i), multiplier);
                    boolean noSolution = solutionContext.checkNoSolutionVariants(system, outFile);
                    if (noSolution) {
                        return false;
                    }

                }
            }
        }
        return true;
    }

    private void makeForm() {
        for (int i = LinearEquation.numberOfCoefficients - 2; i >= 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                ComplexNumber complexNumber1 = (ComplexNumber) system.get(j).getCoefficientByPosition(i);
                ComplexNumber complexNumber2 = (ComplexNumber) system.get(i).getCoefficientByPosition(i);
                ComplexNumber multiplier = complexNumber1.divide(complexNumber2);
                if (!multiplier.getRealPart().equals(BigDecimal.valueOf(0))
                        || !multiplier.getImaginaryPart().equals(BigDecimal.valueOf(0))) {
                    subtractRows(system.get(j), system.get(i), multiplier);
                }
            }
        }
    }

    private void divideRow(Row whichRow, ComplexNumber withComplexNumber) {
        System.out.println(whichRow.getName() + " / " + withComplexNumber + " -> " + whichRow.getName());
        for (int i = 0; i < LinearEquation.numberOfCoefficients; i++) {
            ComplexNumber whichComplexNumber = (ComplexNumber) whichRow.getCoefficientByPosition(i);
            whichRow.replaceCoefficient(i, whichComplexNumber.divide(withComplexNumber));
        }
    }

    private void subtractRows(Row whichRow, Row withRow, ComplexNumber helpMultiplier) {
        System.out.println(helpMultiplier + " * " + withRow.getName() + " + " + whichRow.getName()
                + " -> " + whichRow.getName());

        for (int i = 0; i < LinearEquation.numberOfCoefficients; i++) {
            ComplexNumber complexNumber1 = (ComplexNumber) whichRow.getCoefficientByPosition(i);
            ComplexNumber complexNumber2 = ((ComplexNumber) withRow.getCoefficientByPosition(i)).multiply(helpMultiplier);
            whichRow.replaceCoefficient(i, complexNumber1.subtract(complexNumber2));
        }
    }

    private void searchNonZeroElement(int position, int rowIndex) {

        boolean isSwap = lookBelow(position, rowIndex);
        if (isSwap) {
            return;
        }

        isSwap = lookToTheRight(position, rowIndex);

        if (isSwap) {
            return;
        }

        isSwap = lookInTheWholeLeftPart(position, rowIndex);

        if (isSwap) {
            return;
        }
    }

    // look below and to the right direction of the element in search of a non-zero element
    private boolean lookBelow(int position, int rowIndex) {
        for (int i = rowIndex + 1; i < system.size(); i++) {
            ComplexNumber complexNumber = (ComplexNumber) system.get(i).getCoefficientByPosition(position);
            if (!complexNumber.getImaginaryPart().equals(BigDecimal.valueOf(0))
                    || !complexNumber.getRealPart().equals(BigDecimal.valueOf(0))) {
                swapRows(rowIndex, i);

                return true;
            }
        }
        return false;
    }

    //look to the right of element and looking for non-zero value. If true, switch the columns
    private boolean lookToTheRight(int position, int rowIndex) {
        for (int i = position + 1; i < LinearEquation.numberOfCoefficients - 1; i++) {
            ComplexNumber complexNumber = (ComplexNumber) system.get(rowIndex).getCoefficientByPosition(i);
            if (!complexNumber.getRealPart().equals(BigDecimal.valueOf(0))
                    || !complexNumber.getImaginaryPart().equals(BigDecimal.valueOf(0))) {
                swapColumns(position, i, false);

                return true;
            }
        }
        return false;
    }


    private boolean lookInTheWholeLeftPart(int position, int rowIndex) {
        for (int i = 0; i < system.size(); i++) {
            for (int j = 0; j < LinearEquation.numberOfCoefficients - 1; j++) {
                ComplexNumber complexNumber = (ComplexNumber) system.get(i).getCoefficientByPosition(j);
                if (!complexNumber.getRealPart().equals(BigDecimal.valueOf(0))
                        || !complexNumber.getImaginaryPart().equals(BigDecimal.valueOf(0))) {
                    swapColumns(position, j, false);
                    swapRows(rowIndex, i);
                    return true;
                }
            }
        }
        return false;
    }

    //which - индекс уравнения, которое мы меняем. with - индекс уравнения на которое мы меняем
    private void swapRows(int which, int with) {
        System.out.println(system.get(which).getName() + " <-> " + system.get(with).getName());

        String tempName = system.get(which).getName();
        Row temp = system.get(which);

        system.get(which).setName(system.get(with).getName());
        system.set(which, system.get(with));

        system.get(with).setName(tempName);
        system.set(with, temp);
    }

    //think about isBack
    private void swapColumns(int which, int with, boolean isBack) {
        System.out.println("Swap column: " + (which + 1) + " to " + (with + 1));

        ComplexNumber[] tempCoefficients = new ComplexNumber[system.size()];

        for (int i = 0; i < tempCoefficients.length; i++) {
            tempCoefficients[i] = (ComplexNumber) system.get(i).getCoefficientByPosition(which);
        }

        for (Row row : system) {
            row.replaceCoefficient(which, row.getCoefficientByPosition(with));
        }

        for (int k = 0; k < system.size(); k++) {
            system.get(k).replaceCoefficient(which, tempCoefficients[k]);
        }
        if (!(isBack)) {
            columnsSwap.add(new SwitchedColumn(which, with));
        }
    }

    private void returnColumnToItPlace() {
        for (SwitchedColumn column : columnsSwap) {
            swapColumns(column.getWithIndex(), column.getWhichIndex(), true);
        }
    }

    private static class SwitchedColumn {
        private int whichIndex;
        private int withIndex;

        SwitchedColumn(int whichIndex, int withIndex) {
            this.whichIndex = whichIndex;
            this.withIndex = withIndex;
        }

        private int getWhichIndex() {
            return whichIndex;
        }

        private int getWithIndex() {
            return withIndex;
        }
    }
}
