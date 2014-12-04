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

	boolean reset = false;
	double weight;
	double avgRegisteredUsers;
	double avgItemsOnSale;
	
	int waveId ;
	
	
	public int getWaveId() {
		return waveId;
	}

	public void setWaveId(int waveId) {
		this.waveId = waveId;
	}

	public void setReset(){
		this.reset = true;
	}
	
	public boolean isReset(){
		return reset;
	}
	

	public double getWeight() {
		return weight;
	}

	public double getAvgRegisteredUsers() {
		return avgRegisteredUsers;
	}

	public void setAvgRegisteredUsers(double avgRegisteredUsers) {
		this.avgRegisteredUsers = avgRegisteredUsers;
	}

	public double getAvgItemsOnSale() {
		return avgItemsOnSale;
	}

	public void setAvgItemsOnSale(double avgItemsOnSale) {
		this.avgItemsOnSale = avgItemsOnSale;
	}

	public double getNodeCount() {
		return Math.round(1/weight);
	}
	
	public double getRegisteredUsers(){
		return Math.round(avgRegisteredUsers/weight);
	}
	
	public double getItemsOnSale(){
		return Math.round(avgItemsOnSale/weight);
	}
	
	
	public void setWeight(double weight) {
		this.weight = weight;
	}

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6867433225365269043L;
	private static double DELTA = 0.000001d;
	
	public String toString(){
		return "WaveId:"+ waveId+" Weight:"+(1/weight)+ " Items:"+(avgItemsOnSale/weight)+ " Users:"+(avgRegisteredUsers/weight);
	}
	
	public GossipDto() {
		weight = 0;
		avgItemsOnSale = 0;
		avgRegisteredUsers = 0;
		waveId = -1;
	}
	public GossipDto(GossipDto g) {
		weight = g.getWeight();
		waveId = g.getWaveId();
		avgItemsOnSale = g.getAvgItemsOnSale();
		avgRegisteredUsers = g.getAvgRegisteredUsers();
	}
	
	
	public boolean sameValues(GossipDto dto){
		return Math.abs(weight - dto.getWeight()) <  DELTA && 
				Math.abs(avgItemsOnSale - dto.getAvgItemsOnSale()) <  DELTA && 
				Math.abs(avgRegisteredUsers - dto.getAvgRegisteredUsers()) <  DELTA;
	}
	
}
