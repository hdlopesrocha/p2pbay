package ist.p2p.logic;

// TODO: Auto-generated Javadoc
/**
 * The Class LogicNode.
 */
public abstract class LogicNode {

	/** The args. */
	private LogicNode[] args;

	/**
	 * Gets the args.
	 *
	 * @return the args
	 */
	public LogicNode[] getArgs() {
		return args;
	}

	/**
	 * Instantiates a new logic node.
	 *
	 * @param args
	 *            the args
	 */
	public LogicNode(LogicNode... args) {
		this.args = args;
	}

	/**
	 * Check.
	 *
	 * @param strings
	 *            the strings
	 * @return true, if successful
	 */
	public abstract boolean check(Iterable<String> strings);

}
