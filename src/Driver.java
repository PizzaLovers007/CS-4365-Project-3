import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Driver {

	/**
	 * Starts up the main part of the program.
	 * @param clauses List of initial Clauses
	 */
    public void go(ArrayList<Clause> clauses) {
        performResolution(clauses);
    }

	
	/**
	 * Main part of the program which runs the algorithm to resolve clauses.
	 * @param clauses List of initial Clauses (knowledge-base)
	 */
    public void performResolution(ArrayList<Clause> clauses) {
        PriorityQueue<Clause> queue = new PriorityQueue<>(); // Queue to select next best clause
        HashSet<Clause> searched = new HashSet<>();  // Clauses that have already been mashed
        HashSet<Clause> willSearch = new HashSet<>();   // Clauses that have been added to the queue before

        // Fill queue with initial clauses
        queue.addAll(clauses);

        // Loop until every clause has been expanded
        while (!queue.isEmpty() && !queue.peek().isFalse()) {
            Clause toMash = queue.remove();

            // Check if clause has been expanded already
            if (searched.contains(toMash)) {
                continue;
            }

            // Checks if clause is subsumed by (more specific than) an already mashed clause
            boolean isSubsumed = false;
            for (Clause other : searched) {
                if (toMash.subsumedBy(other)) {
                    isSubsumed = true;
                    break;
                }
            }
            if (isSubsumed) {
                continue;
            }

            // Mash current clause with all previously mashed clauses
            for (Clause other : searched) {
                if (toMash.canResolve(other)) {
                    Clause mashed = toMash.resolve(other, clauses.size()+1);

                    // Check if mashed clause is already in the queue
                    if (willSearch.contains(mashed)) {
                        continue;
                    }

                    // Add mashed clause to queue and knowledge-base
                    willSearch.add(mashed);
                    queue.add(mashed);
                    clauses.add(mashed);

                    // Check if contradiction was made
                    if (mashed.isFalse()) {
                        break;
                    }
                }
            }

            // Clause has now been mashed before
            searched.add(toMash);
        }

        if (queue.isEmpty()) { // No contradiction found
            System.out.println("Failure");
        } else { // Contradiction was found
            printClauseTree(clauses);
        }
    }

	/**
	 * Print the clause tree in descending order with only the used clauses.
	 * @param clauses List of Clauses
	 */
    public void printClauseTree(ArrayList<Clause> clauses) {
        boolean[] used = new boolean[clauses.size()];
        findClauses(clauses.size()-1, clauses, used); // Find all of the clauses on the solution path
        for (int i = 0; i < used.length; i++) { // Print out all of the clauses on the solution path
            if (used[i]) {
                System.out.println(clauses.get(i));
            }
        }

        System.out.printf("Size of final clause set: %d", clauses.size());
    }

	/**
	 * Used to find the clauses which are on the path to the solution.
	 * @param index Int of the clauses which were used
	 * @param clauses List of Clauses
	 * @param used Boolean given to mark that a clause was used
	 */
    public void findClauses(int index, ArrayList<Clause> clauses, boolean[] used) {
        // Check if already marked
        if (used[index]) {
            return;
        }

        // Mark
        used[index] = true;

        // Stop if from original knowledge-base, otherwise continue with 2 clauses that were mashed
        // to form the current clause
        Clause curr = clauses.get(index);
        if (curr.source.length != 0) {
            findClauses(curr.source[0]-1, clauses, used); // Recurses through the first resolved clause of the given clause
            findClauses(curr.source[1]-1, clauses, used); // Recurses through the second resolved clause of the given clause
        }
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
			
			// Parser
			ArrayList<Clause> clauses = new ArrayList<>();
			Clause c; // Clause to be added
			String readClause;
			Pattern pat = Pattern.compile("~?\\w+"); // Matches strings which can start with ~ and have one or more alphanumeric symbols
			int givenID = 1;
			while(in.hasNextLine()) { // Loop through all lines of input
				c = new Clause(givenID);
				readClause = in.nextLine();
				Matcher mat = pat.matcher(readClause);
				while(mat.find()) { // In each line of input, find valid strings which match the pattern
					String var = mat.group();
					if(var.charAt(0) == '~') {
						c.addVariable(new Variable(var.substring(1),true)); // Add Varible with negation
					}
					else {
						c.addVariable(new Variable(var,false)); // Add Variable without negation
					}
					mat = pat.matcher(mat.replaceFirst("").trim());
				}
				Collections.sort(c.vars);
				clauses.add(c);
				givenID++;
			}

            // Run main program
            new Driver().go(clauses);
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            System.exit(1);
        }
    }
}
