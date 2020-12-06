package hu.bme.mit.theta.k.induction;

import hu.bme.mit.theta.cfa.CFA;
import hu.bme.mit.theta.core.stmt.Stmt;

import java.util.List;

public class PathState {
    int key;
    int parentKey;
    CFA.Loc loc;
    CFA.Edge edge;
    List<Stmt> stmtList;

    PathState(int key, int parentKey, CFA.Loc loc, CFA.Edge edge, List<Stmt> stmtList) {
        this.key = key;
        this.parentKey = parentKey;
        this.loc = loc;
        this.edge = edge;
        this.stmtList = stmtList;
    }

    @Override
    public String toString() {
        return "PathState{" + "key=" + key + ", parentKey=" + parentKey + ", loc=" + loc.getName() + '}';
    }
}