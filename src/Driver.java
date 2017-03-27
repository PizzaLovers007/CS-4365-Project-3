import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Driver {

    public void go(ArrayList<Clause> clauses) {
        priorityQueue(clauses);
    }

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
//            System.out.println(queue.size() + " " + toMash);
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

    public void printClauseTree(ArrayList<Clause> clauses) {
        boolean[] used = new boolean[clauses.size()];
        findClauses(clauses.size()-1, clauses, used);
        for (int i = 0; i < used.length; i++) {
            if (used[i]) {
                System.out.println(clauses.get(i));
            }
        }
        System.out.printf("Size of final clause set: %d", clauses.size());
    }

    public void findClauses(int index, ArrayList<Clause> clauses, boolean[] used) {
        if (used[index]) {
            return;
        }
        used[index] = true;
        Clause curr = clauses.get(index);
        if (curr.source.length != 0) {
            findClauses(curr.source[0]-1, clauses, used);
            findClauses(curr.source[1]-1, clauses, used);
        }
    }

    /**
     * Parses args from the command line.
     * @param args command line arguments
     */
    public static void main(String[] args) {
//		args = new String[1]; //DEBUG
//		args[0] = "longinput.txt"; //DEBUG
        if (args.length != 1) {
            System.out.println("Incorrect number of arguments.");
            System.exit(1);
        }
        try {
            Scanner in = new Scanner(new File(args[0]));

            // Test code
            /*ArrayList<Clause> clauses = new ArrayList<>();
            Clause c = new Clause(1);
            c.addVariable(new Variable("A", false));
            clauses.add(c);
            c = new Clause(2);
            c.addVariable(new Variable("A", true));
            clauses.add(c);*/
			
			
			// Main Parser
			ArrayList<Clause> clauses = new ArrayList<>();
			Clause c;
			String readClause;
			Pattern pat = Pattern.compile("~?\\w+");
			int givenID = 1;
			while(in.hasNextLine())
			{
				c = new Clause(givenID);
				readClause = in.nextLine();
				Matcher mat = pat.matcher(readClause);
				while(mat.find())
				{
					String var = mat.group();
					if(var.charAt(0) == '~')
					{
						c.addVariable(new Variable(var.substring(1),true));
					}
					else
					{
						c.addVariable(new Variable(var,false));
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
