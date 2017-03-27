
/**
 * Object representation of a Variable for a Clause
 */
public class Variable implements Comparable<Variable> {

    String name; //Holds the name of the Variable
    boolean negated; //True if the variable is negated

	/**
	 * Constructor for making a new (@code Variable)
	 */
    public Variable(String name, boolean negated) {
        this.name = name;
        this.negated = negated;
    }

	/**
	 * Determines which Variable 
	 * @param other Variable to compare against
	 * @return positive if other 
	 */
	 //TODO: Think of a better way to phrase this comment
    public int compareTo(Variable other) {
        if (negated) {
            if (other.negated) {
                return name.compareTo(other.name);
            }
            return -1;
        } else if (other.negated) {
            return 1;
        }
        return name.compareTo(other.name);
    }

	/**
	 * Check to see if two Variables are the same
	 * @param obj Object to check against for equality
	 * @return true if a Variable and obj are equal
	 */
    public boolean equals(Object obj) {
        if (obj instanceof Variable) {
            Variable other = (Variable)obj;
            return negated == other.negated && name.equals(other.name);
        }
        return false;
    }

	/**
	 * Create a hash in terms of a Variable's negation and name
	 * @return int for a Variable's hash
	 */
    public int hashCode() {
        return negated ? -name.hashCode() : name.hashCode();
    }
	
	public String toString() {
        return String.format("%s%s", negated ? "~" : "", name);
    }
}
