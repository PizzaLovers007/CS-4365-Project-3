import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Driver {

    public void go(ArrayList<Clause> clauses) {
        greedy(clauses);
    }

    public void greedy(ArrayList<Clause> clauses) {
        while (!clauses.get(clauses.size()-1).isFalse()) {
            Clause toMash = clauses.get(clauses.size()-1);
            for (Clause other : clauses) {
                if (toMash.canResolve(other)) {
                    clauses.add(toMash.resolve(other, clauses.size()+1));
                    break;
                }
            }
        }
        for (Clause c : clauses) {
            System.out.println(c);
        }
        System.out.printf("Size of final clause set: %d", clauses.size());
    }

    /**
     * Parses args from the command line.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Incorrect number of arguments.");
            System.exit(1);
        }
        try {
            Scanner in = new Scanner(new File(args[0]));

            // Test code
            ArrayList<Clause> clauses = new ArrayList<>();
            Clause c = new Clause(1);
            c.addVariable(new Variable("A", false));
            clauses.add(c);
            c = new Clause(2);
            c.addVariable(new Variable("A", true));
            clauses.add(c);

            // Run main program
            new Driver().go(clauses);
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            System.exit(1);
        }
    }
}
