package ist.p2p.logic;

// TODO: Auto-generated Javadoc
/**
 * The Class LogicAnd.
 */
public class LogicAnd extends LogicNode {

	/**
	 * Instantiates a new logic and.
	 *
	 * @param args
	 *            the args
	 */
	public LogicAnd(LogicNode... args) {
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
			if (!node.check(strings))
				return false;
		}
		return true;
	}

}
