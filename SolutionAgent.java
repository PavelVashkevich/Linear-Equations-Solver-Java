package range;

import java.util.ArrayList;

public interface SolutionAgent {

    boolean isInfinitelyManySolutions(ArrayList<Row> system);

    boolean isNoSolution(ArrayList<Row> system);

    void findSolution(ArrayList<Row> system, String fileName);
}
