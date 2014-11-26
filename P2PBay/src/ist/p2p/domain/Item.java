package ist.p2p.domain;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class ItemDto.
 */
public class Item implements Serializable, Domain {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7647175142472873433L;

	/** The owner. */
	private String owner;

	/** The title. */
	private String title;

	/** The description. */
	private String description;



	
	private boolean closed;
	
	/**
	 * Instantiates a new item dto.
	 */
	public Item() {
		super();
		closed = false;
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
		closed = false;
	}

	public boolean isClosed(){
		return closed;
	}
	
	public void close(){
		closed = true;
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

	@Override
	public DomainType getDomainType(){
		return DomainType.ITEM;
	}

}
