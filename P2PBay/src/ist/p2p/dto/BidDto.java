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
public class BidDto implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4729470545966153395L;

	/** The offer. */
	private float offer;

	/** The user. */
	private String user;

	/** The ID of Item. */
	private String Item;

	/**
	 * Instantiates a new bid dto.
	 */
	public BidDto() {
		super();
	}

	/**
	 * Instantiates a new bid dto.
	 *
	 * @param offer
	 *            the offer
	 * @param user
	 *            the user
	 * @param Item
	 *            the item
	 */
	public BidDto(float offer, String user, String Item) {
		super();
		this.setOffer(offer);
		this.setUser(user);
		this.setItem(Item);
	}

	/**
	 * Gets the item.
	 *
	 * @return the item
	 */
	public String getItem() {
		return Item;
	}

	/**
	 * Sets the item.
	 *
	 * @param Item
	 *            the new item
	 */
	private void setItem(String Item) {
		this.Item = Item;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Sets the user.
	 *
	 * @param user
	 *            the new user
	 */
	private void setUser(String user) {
		this.user = user;
	}

	/**
	 * Gets the offer.
	 *
	 * @return the offer
	 */
	public float getOffer() {
		return offer;
	}

	/**
	 * Sets the offer.
	 *
	 * @param offer
	 *            the new offer
	 */
	private void setOffer(float offer) {
		this.offer = offer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "{user:" + user + ", offer:'" + offer + "'}";
	}

}
