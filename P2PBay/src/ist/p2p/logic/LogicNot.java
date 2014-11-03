package ist.p2p.logic;

// TODO: Auto-generated Javadoc
/**
 * The Class LogicNot.
 */
public class LogicNot extends LogicNode {

	/** The not. */
	private LogicNode not;

	/**
	 * Instantiates a new logic not.
	 *
	 * @param arg
	 *            the arg
	 */
	public LogicNot(LogicNode arg) {
		this.not = arg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ist.p2p.logic.LogicNode#check(java.lang.Iterable)
	 */
	@Override
	public boolean check(Iterable<String> strings) {
		return !not.check(strings);
	}

}
