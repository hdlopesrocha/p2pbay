package ist.p2p.logic;

// TODO: Auto-generated Javadoc
/**
 * The Class LogicString.
 */
public class LogicString extends LogicNode {

	/** The content. */
	private String content;

	/**
	 * Instantiates a new logic string.
	 *
	 * @param content
	 *            the content
	 */
	public LogicString(String content) {
		this.content = content;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ist.p2p.logic.LogicNode#check(java.lang.Iterable)
	 */
	@Override
	public boolean check(Iterable<String> strings) {
		for (String s : strings) {
			if (content.equals(s))
				return true;
		}

		return false;
	}

	 @Override
	public String toString(){
		return content;
	}
}
