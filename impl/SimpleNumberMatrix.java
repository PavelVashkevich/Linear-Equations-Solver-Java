package range.impl;

import range.LinearEquation;
import range.Matrix;
import range.Row;

import java.util.ArrayList;

public class SimpleNumberMatrix implements Matrix {
    private ArrayList<Row> system;
    private SolutionContext solutionContext;
    private ArrayList<SwitchedColumn> columnsSwap;

    public SimpleNumberMatrix(ArrayList<Row> system) {
        this.system = system;
        solutionContext = new SolutionContext();
        solutionContext.setMethod(new SimpleNumberSolutionAgent());
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

            if (system.get(i).getCoefficientByPosition(i).equals(0.0)) {

                searchNonZeroElement(i, i);
            } else if (!system.get(i).getCoefficientByPosition(i).equals(1.0)) {
                multiplyRow(system.get(i), 1 / (Double) system.get(i).getCoefficientByPosition(i));
            }

            for (int j = i + 1; j < system.size(); j++) {
                double multiplier = (Double) system.get(j).getCoefficientByPosition(i) / (Double) system.get(i).getCoefficientByPosition(i);
                if (multiplier != 0 && Double.isFinite(multiplier)) {
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
                double multiplier = (Double) system.get(j).getCoefficientByPosition(i) / (Double) system.get(i).getCoefficientByPosition(i);

                if (multiplier != 0 && Double.isFinite(multiplier)) {
                    subtractRows(system.get(j), system.get(i), multiplier);
                }
            }
        }
    }

    private void subtractRows(Row whichRow, Row withRow, double helpMultiplier) {
        System.out.println(-helpMultiplier + " * " + withRow.getName() + " + " + whichRow.getName()
                + " -> " + whichRow.getName());

        for (int i = 0; i < LinearEquation.numberOfCoefficients; i++) {
            whichRow.replaceCoefficient(i, (Double) whichRow.getCoefficientByPosition(i) - helpMultiplier * (Double) withRow.getCoefficientByPosition(i));
        }
    }

    private void multiplyRow(Row whichRow, double multiplier) {
        System.out.println(multiplier + " * " + whichRow.getName() + " -> " + whichRow.getName());

        for (int i = 0; i < LinearEquation.numberOfCoefficients; i++) {
            whichRow.replaceCoefficient(i, multiplier * (Double) whichRow.getCoefficientByPosition(i));
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
            if (!system.get(i).getCoefficientByPosition(position).equals(0.0)) {
                swapRows(rowIndex, i);

                return true;
            }
        }
        return false;
    }

    //look to the right of element and looking for non-zero value. If true, switch the columns
    private boolean lookToTheRight(int position, int rowIndex) {
        for (int i = position + 1; i < LinearEquation.numberOfCoefficients - 1; i++) {
            if (!system.get(rowIndex).getCoefficientByPosition(i).equals(0.0)) {
                swapColumns(position, i, false);

                return true;
            }
        }
        return false;
    }


    private boolean lookInTheWholeLeftPart(int position, int rowIndex) {
        for (int i = 0; i < system.size(); i++) {
            for (int j = 0; j < LinearEquation.numberOfCoefficients - 1; j++) {
                if (!system.get(i).getCoefficientByPosition(j).equals(0.0)) {
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

    private void swapColumns(int which, int with, boolean isBack) {
        System.out.println("Swap column: " + (which + 1) + " to " + (with + 1));

        Double[] tempCoefficients = new Double[system.size()];

        for (int i = 0; i < tempCoefficients.length; i++) {
            tempCoefficients[i] = (Double) system.get(i).getCoefficientByPosition(which);
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
