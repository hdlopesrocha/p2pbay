package ist.p2p.dto;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class ItemDto.
 */
public class ItemDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7647175142472873433L;

	/** The owner. */
	private String owner;

	/** The title. */
	private String title;

	/** The description. */
	private String description;

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
	public ItemDto(String owner, String title, String description) {
		super();
		this.owner = owner;
		this.title = title;
		this.description = description;
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

	public String toString() {
		return title+":"+description;
	}
	
}
