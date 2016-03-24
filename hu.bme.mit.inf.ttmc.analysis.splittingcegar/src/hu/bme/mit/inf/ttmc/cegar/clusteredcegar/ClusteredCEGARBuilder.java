package hu.bme.mit.inf.ttmc.cegar.clusteredcegar;

import hu.bme.mit.inf.ttmc.cegar.clusteredcegar.data.ClusteredAbstractState;
import hu.bme.mit.inf.ttmc.cegar.clusteredcegar.data.ClusteredAbstractSystem;
import hu.bme.mit.inf.ttmc.cegar.clusteredcegar.steps.ClusteredChecker;
import hu.bme.mit.inf.ttmc.cegar.clusteredcegar.steps.ClusteredConcretizer;
import hu.bme.mit.inf.ttmc.cegar.clusteredcegar.steps.ClusteredInitializer;
import hu.bme.mit.inf.ttmc.cegar.clusteredcegar.steps.ClusteredRefiner;
import hu.bme.mit.inf.ttmc.cegar.clusteredcegar.utils.ClusteredCEGARDebugger;
import hu.bme.mit.inf.ttmc.cegar.common.GenericCEGARLoop;
import hu.bme.mit.inf.ttmc.cegar.common.ICEGARBuilder;
import hu.bme.mit.inf.ttmc.cegar.common.utils.visualization.IVisualizer;
import hu.bme.mit.inf.ttmc.cegar.common.utils.visualization.NullVisualizer;
import hu.bme.mit.inf.ttmc.common.logging.Logger;
import hu.bme.mit.inf.ttmc.common.logging.impl.NullLogger;

public class ClusteredCEGARBuilder implements ICEGARBuilder {
	private Logger logger = new NullLogger();
	private IVisualizer visualizer = new NullVisualizer();
	private ClusteredCEGARDebugger debugger = null;

	/**
	 * Set logger
	 *
	 * @param logger
	 * @return Builder instance
	 */
	public ClusteredCEGARBuilder logger(final Logger logger) {
		this.logger = logger;
		return this;
	}

	/**
	 * Set visualizer
	 *
	 * @param visualizer
	 * @return Builder instance
	 */
	public ClusteredCEGARBuilder visualizer(final IVisualizer visualizer) {
		this.visualizer = visualizer;
		return this;
	}

	public ClusteredCEGARBuilder debug(final IVisualizer visualizer) {
		if (visualizer == null)
			debugger = null;
		else
			debugger = new ClusteredCEGARDebugger(visualizer);
		return this;
	}

	/**
	 * Build CEGAR loop instance
	 *
	 * @return CEGAR loop instance
	 */
	@Override
	public GenericCEGARLoop<ClusteredAbstractSystem, ClusteredAbstractState> build() {
		return new GenericCEGARLoop<ClusteredAbstractSystem, ClusteredAbstractState>(new ClusteredInitializer(logger, visualizer),
				new ClusteredChecker(logger, visualizer), new ClusteredConcretizer(logger, visualizer), new ClusteredRefiner(logger, visualizer), debugger,
				logger, "Clustered");
	}
}
