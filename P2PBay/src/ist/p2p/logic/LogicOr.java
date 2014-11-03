package ist.p2p.logic;

// TODO: Auto-generated Javadoc
/**
 * The Class LogicOr.
 */
public class LogicOr extends LogicNode {

	/**
	 * Instantiates a new logic or.
	 *
	 * @param args
	 *            the args
	 */
	public LogicOr(LogicNode... args) {
		super(args);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ist.p2p.logic.LogicNode#check(java.lang.Iterable)
	 */
	@Override
	public boolean check(Iterable<String> strings) {
		for (LogicNode node : getArgs()) {
			if (node.check(strings))
				return true;
		}
		return false;
	}

}
