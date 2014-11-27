/**
 * 
 */
package ist.p2p.dto;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class BidDto.
 *
 * @author Angelo Pingo
 */
public class GossipDto implements Serializable {

	double score = 1d;
	boolean reset = false;
	double weight = 0d;

	public void setReset(){
		this.reset = true;
	}
	
	public boolean isReset(){
		return reset;
	}
	
	public double getNodeCount(){
		return 1/weight;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6867433225365269043L;

	public GossipDto() {
	}
	
	

}
