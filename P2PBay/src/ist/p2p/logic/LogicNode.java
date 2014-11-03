package ist.p2p.logic;

import java.util.ArrayList;
import java.util.List;

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

	private static LogicNode rec(String str, int dep) {
	
		List<String> tokens = getTokens(str.charAt(0)=='(' ? str.substring(1,str.length()-1) : str);
		
		if (str.startsWith("(and")) {
			tokens.remove(0);
			List<LogicNode> list = new ArrayList<LogicNode>();
			for (String s : tokens)
				list.add(rec(s, dep + 1));
			return new LogicAnd(list.toArray(new LogicNode[list.size()]));
		} else if (str.startsWith("(or")) {
			tokens.remove(0);
			List<LogicNode> list = new ArrayList<LogicNode>();
			for (String s : tokens)
				list.add(rec(s, dep + 1));
			return new LogicOr(list.toArray(new LogicNode[list.size()]));
		} else if (str.startsWith("(not")) {
			tokens.remove(0);
			return new LogicNot(rec(tokens.get(0), dep + 1));
			// return new LogicNot(subNodes.get(0));
		} else {
			return new LogicString(str);
		}
	}

	
	private static List<String> getTokens(String str){
		
		List<String> tokens = new ArrayList<String>();
		
		String token ="";
		int depth = 0;
		
		for(int i=0; i < str.length() ; ++i){
			char c = str.charAt(i);
			
			if(c == '('){
				++depth;
				token += c;
			}
			else if(c == ')'){
				--depth;
				token += c;
			}
			else if(c==' ' && depth==0){
				tokens.add(token);
				token ="";
			}
			else {
				token += c;
			}
		}
		
		
		
		tokens.add(token);

		return tokens;
	}
	
	public static LogicNode extractFromString(String str) {
		return rec(str, 0);
	}

}
