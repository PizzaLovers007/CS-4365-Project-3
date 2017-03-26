import java.util.ArrayList;
import java.util.Collections;

public class Clause implements Comparable<Clause> {

    ArrayList<Variable> vars;
    private Variable toMash;
    int id;
    int[] source;

    public Clause(int id) {
        vars = new ArrayList<>();
        this.id = id;
        source = new int[0];
    }

    public boolean addVariable(Variable toAdd) {
        for (Variable v : vars) {
            if (v.name.equals(toAdd.name)) {
                return false;
            }
        }
        return vars.add(toAdd);
    }

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

    public boolean isFalse() {
        return vars.size() == 0;
    }

    public String toString() {
        return String.format("%d. %s \t{%s}",
                id,
                isFalse() ? "False" : vars.toString().replaceAll("[\\[\\],]", ""),
                source.length == 0 ? "" : String.format("%d,%d", source[0], source[1]));
    }

    public int compareTo(Clause other) {
        if (vars.size() == other.vars.size()) {
            return id - other.id;
        }
        return vars.size() - other.vars.size();
    }

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

    public int hashCode() {
        int sum = 0;
        for (Variable v : vars) {
            sum += v.hashCode();
        }
        return sum;
    }
}
