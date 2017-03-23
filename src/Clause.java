import java.util.ArrayList;

public class Clause {

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
}
