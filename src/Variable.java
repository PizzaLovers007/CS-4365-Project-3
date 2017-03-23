public class Variable {

    String name;
    boolean negated;

    public Variable(String name, boolean negated) {
        this.name = name;
        this.negated = negated;
    }

    public String toString() {
        return String.format("%s%s", negated ? "~" : "", name);
    }
}
