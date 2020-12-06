package hu.bme.mit.theta.k.induction;

import hu.bme.mit.theta.analysis.Trace;
import hu.bme.mit.theta.cfa.CFA;

import java.util.concurrent.TimeUnit;

public class KInductionResult {

    private enum enumResult {
        Safe,
        Unsafe,
        Unknown
    }
    private enumResult result;
    private Trace<CFA.Loc, CFA.Edge> trace;
    private long time;
    private int depth;

    public KInductionResult(long time, int depth) {
        result = enumResult.Unknown;

        this.trace = null;

        this.time = time;
        this.depth = depth;
    }

    public KInductionResult(boolean isSafe, long time, int depth) {
        assert isSafe : "isSafe is not true";

        result = enumResult.Safe;

        this.trace = null;

        this.time = time;
        this.depth = depth;
    }

    public KInductionResult(boolean isUnsafe, Trace<CFA.Loc, CFA.Edge> trace, long time, int depth) {
        assert isUnsafe : "isUnsafe is not true";

        result = enumResult.Unsafe;

        this.trace = trace;

        this.time = time;
        this.depth = depth;
    }

    @Override
    public String toString() {
        String returnString = "";

        returnString += "Result: " + result + "\n";
        if (isUnsafe()) {
            returnString += trace.toString() + "\n";
        }
        returnString += "Time: " + time + "s\n";
        returnString += "Depth: " + depth;

        return returnString;
    }

    public Trace<CFA.Loc, CFA.Edge> getTrace() {
        assert result == enumResult.Unsafe : "to get trace, result must be unsafe";
        return trace;
    }

    public enumResult getResult() { return result; }

    public boolean isSafe() { return result == enumResult.Safe; }

    public boolean isUnsafe() { return result == enumResult.Unsafe; }

    public boolean isUnknown() { return result == enumResult.Unknown; }

    public long getTime() { return time; }

    public int getDepth() { return depth; }
}
