package range.impl;

import range.LinearEquation;
import range.Row;
import range.SolutionAgent;

import java.util.ArrayList;

class SimpleNumberSolutionAgent implements SolutionAgent {


    @Override
    public void findSolution(ArrayList<Row> system, String fileName) {
        ArrayList<Double> solution = getSolution(system);
        new SolutionFileWriterImpl().printSolution(solution, fileName);
    }

    private ArrayList<Double> getSolution(ArrayList<Row> system) {
        ArrayList<Double> solution = new ArrayList<>();

        for (int i = 0; i < LinearEquation.numberOfCoefficients - 1; i++) {
            Double variable = (Double) system.get(i).getConstant() / (Double) system.get(i).getCoefficientByPosition(i);
            solution.add(variable);
        }

        return solution;
    }

    @Override
    public boolean isNoSolution(ArrayList<Row> system) {
        for (Row row : system) {
            int zeroCount = 0;
            for (int j = 0; j < LinearEquation.numberOfCoefficients - 1; j++) {
                if (row.getCoefficientByPosition(j).equals(0.0)) {
                    zeroCount++;
                }
            }
            if (zeroCount == LinearEquation.numberOfCoefficients - 1 && !(row.getConstant().equals(0.0))) {
                return true;

            }
        }
        return false;
    }

    /*
     * infinitely many solutions are when number of significant equations is less than number of significant variables
     * */
    @Override
    public boolean isInfinitelyManySolutions(ArrayList<Row> system) {
        //give this equation one else chance
        for (int i = 0; i < LinearEquation.numberOfCoefficients - 1; i++) {
            for (int j = i; j < system.size() - 1; j++) {
                Double value = (Double) system.get(j + 1).getCoefficientByPosition(i) / (Double) system.get(i).getCoefficientByPosition(i);
                if (!value.equals(0.0) && Double.isFinite(value)) {
                    return false;
                }
            }
        }
        int numberOfSignificantEquals = 0;
        for (Row row : system) {
            for (int j = 0; j < LinearEquation.numberOfCoefficients - 1; j++) {
                if (!row.getCoefficientByPosition(j).equals(0.0)) {
                    numberOfSignificantEquals++;
                    break;
                }
            }
        }
        return numberOfSignificantEquals < LinearEquation.numberOfCoefficients - 1;
    }
}
