import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Driver {

    public void go(ArrayList<Clause> clauses) {
        greedy(clauses);
    }

    public void greedy(ArrayList<Clause> clauses) {
        while (!clauses.get(clauses.size()-1).isFalse()) {
            Clause toMash = clauses.get(clauses.size()-1);
			System.out.println(toMash);
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
		args = new String[1]; //DEBUG
		args[0] = "longinput.txt"; //DEBUG
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
