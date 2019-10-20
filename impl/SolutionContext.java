package range.impl;

import range.Row;
import range.SolutionAgent;

import java.util.ArrayList;

class SolutionContext {
    private SolutionAgent solutionAgent;

    void setMethod(SolutionAgent solutionAgent) {
        this.solutionAgent = solutionAgent;
    }

    boolean checkNoSolutionVariants(ArrayList<Row> system, String fileName) {

        boolean isNoSolutionExist = this.solutionAgent.isNoSolution(system);

        if (isNoSolutionExist) {
            new SolutionFileWriterImpl().printSolution("No solutions", fileName);
            return true;
        }

        isNoSolutionExist = this.solutionAgent.isInfinitelyManySolutions(system);

        if (isNoSolutionExist) {
            new SolutionFileWriterImpl().printSolution("Infinitely many solutions", fileName);
            return true;
        }
        return false;
    }

    void findSolution(ArrayList<Row> system, String fileName) {
        this.solutionAgent.findSolution(system, fileName);
    }
}
