import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Driver {

	/**
	 * Starts up the main part of the program
	 * @param clauses List of initial Clauses
	 */
    public void go(ArrayList<Clause> clauses) {
        priorityQueue(clauses);
    }

	
	/**
	 * Main part of the program which runs the algorithm to resolve clauses
	 * @param clauses List of initial Clauses
	 */
    public void priorityQueue(ArrayList<Clause> clauses) {
        PriorityQueue<Clause> queue = new PriorityQueue<>();
        HashSet<Clause> searched = new HashSet<>();
        HashSet<Clause> willSearch = new HashSet<>();
        queue.addAll(clauses);
        while (!queue.isEmpty() && !queue.peek().isFalse()) {
            Clause toMash = queue.remove();
            if (searched.contains(toMash)) {
                continue;
            }
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
            for (Clause other : searched) {
                if (toMash.canResolve(other)) {
                    Clause mashed = toMash.resolve(other, clauses.size()+1);
                    if (willSearch.contains(mashed)) {
                        continue;
                    }
                    willSearch.add(mashed);
                    queue.add(mashed);
                    clauses.add(mashed);
                    if (mashed.isFalse()) {
                        break;
                    }
                }
            }
            searched.add(toMash);
        }
        if (queue.isEmpty()) {
            System.out.println("Failure");
        } else {
            printClauseTree(clauses);
        }
    }

	/**
	 * Print the clause tree in descending order with only the used clauses
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
	 * Used to find the clauses which are on the path to the solution
	 * @param index Int of the clauses which were used
	 * @param clauses List of Clauses
	 * @param used Boolean given to mark that a clause was used
	 */
    public void findClauses(int index, ArrayList<Clause> clauses, boolean[] used) {
        if (used[index]) {
            return;
        }
        used[index] = true;
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
