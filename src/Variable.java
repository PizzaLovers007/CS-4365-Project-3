public class Variable implements Comparable<Variable> {

    String name;
    boolean negated;

    public Variable(String name, boolean negated) {
        this.name = name;
        this.negated = negated;
    }

    public String toString() {
        return String.format("%s%s", negated ? "~" : "", name);
    }

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

    public boolean equals(Object obj) {
        if (obj instanceof Variable) {
            Variable other = (Variable)obj;
            return negated == other.negated && name.equals(other.name);
        }
        return false;
    }

    public int hashCode() {
        return negated ? -name.hashCode() : name.hashCode();
    }
}
