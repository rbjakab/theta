package hu.bme.mit.theta.k.induction;

import hu.bme.mit.theta.analysis.expl.ExplOrd;
import hu.bme.mit.theta.cfa.CFA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PathOperator {

    private static final class LazyHolder {
        private static final PathOperator INSTANCE = new PathOperator();
    }

    private PathOperator() {

    }

    public static PathOperator getInstance() {
        return PathOperator.LazyHolder.INSTANCE;
    }

    public List<CFA.Loc> listConvertPathVertexToCFALoc(List<PathVertex> path) {
        List<CFA.Loc> returnList = new ArrayList<>();

        for (PathVertex pv : path) {
            returnList.add(pv.loc);
        }

        return returnList;
    }

    public List<CFA.Edge> listConvertPathVertexToCFAEdge(List<PathVertex> path) {
        List<CFA.Edge> returnList = new ArrayList<>();

        for (int i = 0; i < path.size() - 1; i++) {
            returnList.add(path.get(i).edge);
        }

        return returnList;
    }

    public List<PathVertex> getPathVertexPathToInit(HashMap<Integer, PathVertex> pathMap, PathVertex item) {
        List<PathVertex> returnList = new ArrayList<>();

        while (pathMap.get(item.key).parentKey != -1) {
            returnList.add(pathMap.get(item.key));
            item = pathMap.get(item.parentKey);
        }
        returnList.add(pathMap.get(item.key));

        return returnList;
    }
}
