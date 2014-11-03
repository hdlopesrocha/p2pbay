package ist.p2p.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

// TODO: Auto-generated Javadoc
/**
 * The Class ItemDto.
 */
public class ItemDto implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7647175142472873433L;

	/** The owner. */
	private String owner;

	/** The title. */
	private String title;

	/** The description. */
	private String description;

	/** The id. */
	private String id;

	/** The bids. */
	private List<BidDto> bids;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Instantiates a new item dto.
	 */
	public ItemDto() {
		super();
		bids = new ArrayList<BidDto>();
	}

	/**
	 * Gets the bids.
	 *
	 * @return the bids
	 */
	public List<BidDto> getBids() {
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
	public ItemDto(String id, String owner, String title, String description) {
		super();
		this.id = id;
		this.owner = owner;
		this.title = title;
		this.description = description;
		this.bids = new ArrayList<BidDto>();
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
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}



}
