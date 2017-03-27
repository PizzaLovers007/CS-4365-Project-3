import java.util.ArrayList;
import java.util.Collections;

public class Clause implements Comparable<Clause> {

    ArrayList<Variable> vars; //List of Variables in a Clause
    private Variable toMash; //Variable which is going to be resolved in a step
    int id; //Given id of a Clause
    int[] source; //Provides the two source Clauses for a resolved Clause

	/**
	 * Contructor for making a new (@code Clause)
	 */
    public Clause(int id) {
        vars = new ArrayList<>();
        this.id = id;
        source = new int[0];
    }

	/**
	 * Adds a variable to the clause
	 * @param toAdd Variable to be added
	 * @return true if the variable is not already in the Clause
	 */
    public boolean addVariable(Variable toAdd) {
        for (Variable v : vars) {
            if (v.name.equals(toAdd.name)) {
                return false;
            }
        }
        return vars.add(toAdd);
    }

	/**
	 * Checks to see if this clause and another clause can be resolved
	 * @param other Clause to check resolution with
	 * @return true if only one variable can be resolved 
	 */
    public boolean canResolve(Clause other) {
        int oppositeCount = 0;
        for (Variable v1 : vars) {
            for (Variable v2 : other.vars) {
                if (v1.name.equals(v2.name) && v1.negated != v2.negated) {
                    toMash = v1;
                    oppositeCount++;
                    break;
                }
            }
        }
        return oppositeCount == 1;
    }

	/**
	 * Resolves two clauses
	 * @param other Clause to be resolved with
	 * @param id int which give the clause's number in the list
	 * @return resolved Clause
	 */
    public Clause resolve(Clause other, int id) {
        Clause mashed = new Clause(id);
        for (Variable v : vars) {
            if (!v.name.equals(toMash.name)) {
                mashed.addVariable(v);
            }
        }
        for (Variable v : other.vars) {
            if (!v.name.equals(toMash.name)) {
                mashed.addVariable(v);
            }
        }
        mashed.source = new int[] {other.id, this.id};
        Collections.sort(mashed.vars);
        return mashed;
    }

	/**
	 * Sees if resolution is finished and a contradiction is found
	 * @return true if there are no more Variables in the Clause
	 */
    public boolean isFalse() {
        return vars.size() == 0;
    }

	/**
	 *
	 * @param other 
	 * @return
	 */
    public boolean subsumedBy(Clause other) {
        for (Variable v : other.vars) {
            if (!vars.contains(v)) {
                return false;
            }
        }
        return true;
    }

	/**
	 * Compares two clauses to see which one has more variables or has a higher id number
	 * @param other Clause to be compared against
	 * @return positive if the amount of variables in this clause is bigger, otherwise if the id is bigger
	 */
    public int compareTo(Clause other) {
        if (vars.size() == other.vars.size()) {
            return id - other.id;
        }
        return vars.size() - other.vars.size();
    }

	/**
	 * Checks to see if two clauses are equal
	 * @param obj Clause to be checked against
	 * @return true if equal
	 */
    public boolean equals(Object obj) {
        if (obj instanceof Clause) {
            Clause other = (Clause)obj;
            if (vars.size() != other.vars.size()) {
                return false;
            }
            for (int i = 0; i < vars.size(); i++) {
                if (!vars.get(i).equals(other.vars.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

	/**
	 * Create a hash for the Clause using the variables in the Clause
	 * @return int for Clause's hash
	 */
    public int hashCode() {
        int sum = 0;
        for (Variable v : vars) {
            sum += v.hashCode();
        }
        return sum;
    }
	
	public String toString() {
        return String.format("%d. %s \t{%s}",
                id,
                isFalse() ? "False" : vars.toString().replaceAll("[\\[\\],]", ""),
                source.length == 0 ? "" : String.format("%d,%d", source[0], source[1]));
    }
}
