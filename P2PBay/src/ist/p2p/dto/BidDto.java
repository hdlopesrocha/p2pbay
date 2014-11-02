/**
 * 
 */
package ist.p2p.dto;

import java.io.Serializable;

/**
 * @author Angelo Pingo
 *
 */
public class BidDto implements Serializable{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4729470545966153395L;
	
	/** The offer. */
	private float offer;
	
	/** The user. */
	private String user;
	
	/** The ID of Item. */
	private ItemDto Item;

	/**
	 * 
	 */
	public BidDto() {
		super();
	}
	
	public BidDto(float offer, String user, ItemDto Item) {
		super();
		this.setOffer(offer);
		this.setUser(user);
		this.setItem(Item);
	}

	public ItemDto getItem() {
		return Item;
	}

	private void setItem(ItemDto Item) {
		this.Item = Item;
	}

	public String getUser() {
		return user;
	}

	private void setUser(String user) {
		this.user = user;
	}

	public float getOffer() {
		return offer;
	}

	private void setOffer(float offer) {
		this.offer = offer;
	}
	
	public String toString() {
		return "{user:"+user+", offer:'"+offer+"', Item:'"+Item.toString() + "'}";
	}

}
