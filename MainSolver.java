package range;

public class MainSolver {
    public static void main(String[] args) {

        String inFile = null, outFile = null;

        for(int i = 0; i < args.length;i++){
            if(args[i].equalsIgnoreCase("-in")){
                inFile = args[i+1];
                i++;
            }else if(args[i].equalsIgnoreCase("-out")){
                outFile = args[i+1];
                i++;
            }else {
                throw new IllegalArgumentException("Argument is not recognize");
            }
        }

        if(inFile != null && outFile != null) {
            LinearEquation linearEquation = new LinearEquation(inFile);
            linearEquation.solve(outFile);

        }


    }
}

