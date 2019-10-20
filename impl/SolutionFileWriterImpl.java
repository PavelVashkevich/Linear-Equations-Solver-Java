package range.impl;

import range.SolutionFileWriter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class SolutionFileWriterImpl implements SolutionFileWriter {


     public <T> void printSolution(ArrayList<T> solution, String fileName) {
        StringBuilder solutionString = new StringBuilder();

        try (PrintWriter printWriter = new PrintWriter(fileName)) {
            for (int i = 0; i < solution.size(); i++) {
                T variable = solution.get(i);
                printWriter.println(variable);

                if (i < solution.size() - 1) {
                    solutionString.append(variable).append(", ");
                } else {
                    solutionString.append(variable);
                }
            }

            System.out.println("The solution is: " + "(" + solutionString + ")");
            System.out.printf("Saved to file %s", fileName);

        } catch (FileNotFoundException e) {
            System.out.printf("File %s wasn't found", fileName);
        }
    }


     public void printSolution(String solution, String fileName) {
        try (PrintWriter printWriter = new PrintWriter(fileName)) {
            printWriter.println(solution);

            System.out.println(solution);
            System.out.printf("Saved to file %s", fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

