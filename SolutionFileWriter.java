package range;

import java.util.ArrayList;

public interface SolutionFileWriter {

    <T> void printSolution(ArrayList<T> solution, String fileName);

    <T> void printSolution(String solution, String fileName);
}
