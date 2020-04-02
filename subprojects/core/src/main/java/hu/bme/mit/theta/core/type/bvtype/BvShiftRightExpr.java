package hu.bme.mit.theta.core.type.bvtype;

import hu.bme.mit.theta.core.model.Valuation;
import hu.bme.mit.theta.core.type.BinaryExpr;
import hu.bme.mit.theta.core.type.Expr;

import static hu.bme.mit.theta.core.utils.TypeUtils.cast;

public class BvShiftRightExpr extends BinaryExpr<BvType, BvType> {
    private static final int HASH_SEED = 965;
    private static final String OPERATOR_LABEL = ">>";

    private BvShiftRightExpr(final Expr<BvType> leftOp, final Expr<BvType> rightOp) {
        super(leftOp, rightOp);
    }

    public static BvShiftRightExpr of(final Expr<BvType> leftOp, final Expr<BvType> rightOp) {
        return new BvShiftRightExpr(leftOp, rightOp);
    }

    public static BvShiftRightExpr create(final Expr<?> leftOp, final Expr<?> rightOp) {
        final Expr<BvType> newLeftOp = cast(leftOp, (BvType) leftOp.getType());
        final Expr<BvType> newRightOp = cast(rightOp, (BvType) leftOp.getType());
        return BvShiftRightExpr.of(newLeftOp, newRightOp);
    }

    @Override
    public BvType getType() {
        return getOps().get(0).getType();
    }

    @Override
    public BvLitExpr eval(final Valuation val) {
        final BvLitExpr leftOpVal = (BvLitExpr) getLeftOp().eval(val);
        final BvLitExpr rightOpVal = (BvLitExpr) getRightOp().eval(val);
        return leftOpVal.shiftRight(rightOpVal);
    }

    @Override
    public BvShiftRightExpr with(final Expr<BvType> leftOp, final Expr<BvType> rightOp) {
        if (leftOp == getLeftOp() && rightOp == getRightOp()) {
            return this;
        } else {
            return BvShiftRightExpr.of(leftOp, rightOp);
        }
    }

    @Override
    public BvShiftRightExpr withLeftOp(final Expr<BvType> leftOp) {
        return with(leftOp, getRightOp());
    }

    @Override
    public BvShiftRightExpr withRightOp(final Expr<BvType> rightOp) {
        return with(getLeftOp(), rightOp);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof BvShiftRightExpr) {
            final BvShiftRightExpr that = (BvShiftRightExpr) obj;
            return this.getLeftOp().equals(that.getLeftOp()) && this.getRightOp().equals(that.getRightOp());
        } else {
            return false;
        }
    }

    @Override
    protected int getHashSeed() {
        return HASH_SEED;
    }

    @Override
    public String getOperatorLabel() {
        return OPERATOR_LABEL;
    }
}