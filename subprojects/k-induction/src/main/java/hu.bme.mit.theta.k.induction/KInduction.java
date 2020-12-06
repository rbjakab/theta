
package hu.bme.mit.theta.k.induction;

import hu.bme.mit.theta.analysis.Trace;
import hu.bme.mit.theta.cfa.CFA;
import hu.bme.mit.theta.core.stmt.Stmt;
import hu.bme.mit.theta.core.type.Expr;
import hu.bme.mit.theta.core.type.booltype.BoolExprs;
import hu.bme.mit.theta.core.type.booltype.BoolType;
import hu.bme.mit.theta.core.utils.PathUtils;
import hu.bme.mit.theta.core.utils.StmtUnfoldResult;
import hu.bme.mit.theta.core.utils.StmtUtils;
import hu.bme.mit.theta.core.utils.VarIndexing;
import hu.bme.mit.theta.solver.Solver;
import hu.bme.mit.theta.solver.z3.Z3SolverFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class KInduction {

    private CFA cfa;
    private CFA.Loc initLoc;
    private CFA.Loc finalLoc;
    private CFA.Loc errorLoc;
    private long maxTime;
    private int bound;
    private PathOperator pathOperator = PathOperator.getInstance();

    public KInduction(CFA cfa, long maxTime, int bound) {
        this.cfa = cfa;

        initLoc = cfa.getInitLoc();
        finalLoc = cfa.getFinalLoc().get();
        errorLoc = cfa.getErrorLoc().get();

        this.maxTime = maxTime;
        this.bound = bound;
    }

    public KInduction(CFA cfa) {
        this.cfa = cfa;

        initLoc = cfa.getInitLoc();
        finalLoc = cfa.getFinalLoc().get();
        errorLoc = cfa.getErrorLoc().get();

        this.maxTime = -1;
        this.bound = -1;
    }

    public KInductionResult check() {

        /* Solver */
        Solver solver = Z3SolverFactory.getInstance().createSolver();

        /* System level variables */
        int depth = 0;
        int availablePaths;
        long startTime = System.nanoTime();
        long timeInSeconds;

        /* Forward */
        Queue<PathState> queue = new LinkedList<>();
        Queue<PathState> queueTemp = new LinkedList<>();
        HashMap<Integer, PathState> pathMap = new HashMap<Integer, PathState>();

        /* Backward */
        Queue<PathState> queueBW = new LinkedList<>();
        Queue<PathState> queueTempBW = new LinkedList<>();

        /* Initialization forward */
        queue.add(new PathState(0, -1, initLoc, null, new ArrayList<Stmt>()));
        pathMap.put(0, new PathState(0, -1, initLoc, null, new ArrayList<Stmt>()));
        int pathKey = 1;
        /* Initialization backward */
        queueBW.add(new PathState(0, -1, errorLoc, null, new ArrayList<Stmt>()));

        /* Loop */
        while (true) {
            depth++;
            availablePaths = 0;

            timeInSeconds = TimeUnit.SECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
            if (checkOutOfTime(timeInSeconds) || checkBound(depth)) {
                return new KInductionResult(timeInSeconds, depth);
            }

            for (PathState item: queue) {
                for (CFA.Edge edge : item.loc.getOutEdges()) {
                    CFA.Loc nextLoc = edge.getTarget();

                    List<Stmt> stmtList = new ArrayList<>();
                    stmtList.addAll(item.stmtList);
                    stmtList.add(edge.getStmt());

                    PathState nextPS = new PathState(pathKey, item.key, nextLoc, edge, stmtList);
                    pathMap.put(pathKey, nextPS);
                    pathKey++;

                    /* 1 */
                    if (isSat(solver, stmtList)) {
                        queueTemp.add(nextPS);
                        availablePaths++;
                    }
                }
            }
            queueTransfer(queueTemp, queue);

            /* 1 */
            if (availablePaths == 0) {
                timeInSeconds = TimeUnit.SECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
                return new KInductionResult(true, timeInSeconds, depth);
            }

            /* 2 */
            if (!isErrorLocReachable(queueBW, queueTempBW, solver)) {
                timeInSeconds = TimeUnit.SECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
                return new KInductionResult(true, timeInSeconds, depth);
            }

            /* 3 */
            for (PathState item: queue) {
                if (item.loc.equals(errorLoc)) {
                    if (isSat(solver, item.stmtList)) {
                        List<CFA.Loc> states = pathOperator.listConvertPathVertexToCFALoc(pathOperator.getPathVertexPathToInit(pathMap, item));
                        List<CFA.Edge> actions = pathOperator.listConvertPathVertexToCFAEdge(pathOperator.getPathVertexPathToInit(pathMap, item));

                        Trace<CFA.Loc, CFA.Edge> trace = Trace.of(states, actions);

                        timeInSeconds = TimeUnit.SECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
                        return new KInductionResult(true, trace, timeInSeconds, depth);
                    }
                }
            }
        }

        /*
        VarIndexing varIndexing = VarIndexing.all(0);
        CFA.Loc itrLoc = initLoc;
        for (int i = 0; i < 100; i++) {
            CFA.Edge edge = Iterables.get(itrLoc.getOutEdges(), 0);
            CFA.Loc target = edge.getTarget();
            Stmt stmt = edge.getStmt();

            System.out.println("stmt: " + stmt.toString());

            StmtUnfoldResult expr = StmtUtils.toExpr(stmt, varIndexing);
            varIndexing = expr.getIndexing();

            System.out.println("idx: " + varIndexing);
            System.out.println("expr: " + expr.getExprs().toString());

            Expr<BoolType> andExprBool = BoolExprs.And(expr.getExprs());
            System.out.println("andExprBool: " + andExprBool);

            Expr cica = PathUtils.unfold(andExprBool, varIndexing);
            System.out.println("unfold: " + cica);

            System.out.println("------------");

            itrLoc = target;
        }
        */
    }

    private boolean checkOutOfTime(long timeInSeconds) {
        return maxTime != -1 && maxTime < timeInSeconds;
    }

    private boolean checkBound(int depth) {
        return bound != -1 && bound < depth;
    }

    private void writeOutPathFrom(HashMap<Integer, PathState> pathMap, PathState item) {
        List<PathState> writeOutList = pathOperator.getPathVertexPathToInit(pathMap, item);

        for (PathState itr : writeOutList) {
            System.out.println(itr);
        }
    }

    private void queueTransfer(Queue<PathState> copyFrom, Queue<PathState> pasteTo) {
        pasteTo.clear();
        pasteTo.addAll(copyFrom);
        copyFrom.clear();
    }

    private boolean isSat(Solver solver, List<Stmt> stmtList) {
        boolean status;

        StmtUnfoldResult ExprStmt = StmtUtils.toExpr(stmtList, VarIndexing.all(0));

        Expr<BoolType> andExprBool = BoolExprs.And(ExprStmt.getExprs());

        solver.push();
        solver.add(PathUtils.unfold(andExprBool, VarIndexing.all(0)));
        solver.check();

        status = solver.getStatus().isSat();

        solver.pop();

        return status;
    }

    private boolean isErrorLocReachable(Queue<PathState> queueBW, Queue<PathState> queue2BW, Solver solver) {
        int availablePaths = 0;

        for (PathState item: queueBW) {
            for (CFA.Edge edge : item.loc.getInEdges()) {
                CFA.Loc nextLoc = edge.getSource();

                List<Stmt> stmtList = new ArrayList<>();
                stmtList.add(edge.getStmt());
                stmtList.addAll(item.stmtList);
                PathState nextPV = new PathState(0, item.key, nextLoc, edge, stmtList);

                if (isSat(solver, stmtList)) {
                    queue2BW.add(nextPV);
                    availablePaths++;
                }
            }
        }
        queueTransfer(queue2BW, queueBW);

        return availablePaths != 0;
    }

    // TODO
    // private boolean isLoopFree() { return true; }
}