/**
 * 
 */
package seprs.unstructured;

/**
 * @author Angelo Pingo
 *
 */
public class Document {
	
	String text = "";

	/**
	 * 
	 */
	public Document(String text) {
		this.text = text.toLowerCase();
	}
	
	public boolean searchWord(String word) {
		if (this.text.contains(word))
			return true;
		return false;
		
	}

}
