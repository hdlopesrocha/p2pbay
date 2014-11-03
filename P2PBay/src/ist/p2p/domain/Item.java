package ist.p2p.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

// TODO: Auto-generated Javadoc
/**
 * The Class ItemDto.
 */
public class Item implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7647175142472873433L;

	/** The owner. */
	private String owner;

	/** The title. */
	private String title;

	/** The description. */
	private String description;

	/** The description. */
	private String id;

	/** The bids. */
	private Map<Float,String> bids;

	/**
	 * Instantiates a new item dto.
	 */
	public Item() {
		super();
		bids = new TreeMap<Float,String>();
	}

	/**
	 * Gets the bids.
	 *
	 * @return the bids
	 */
	public Map<Float,String> getBids() {
		return bids;
	}

	/**
	 * Instantiates a new item dto.
	 *
	 * @param owner
	 *            the owner
	 * @param title
	 *            the title
	 * @param description
	 *            the description
	 */
	public Item(String owner, String title, String description) {
		super();
		this.owner = owner;
		this.title = title;
		this.description = description;
		this.id = UUID.randomUUID().toString();
		this.bids = new TreeMap<Float,String>();
	}

	/**
	 * Gets the owner.
	 *
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "{id:" + id + ", title:'" + title + "', decription:'"
				+ description + "'}";
	}

}
