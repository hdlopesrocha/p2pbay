package ist.p2p.dto;

import org.json.JSONObject;

// TODO: Auto-generated Javadoc
/**
 * The Class ItemDto.
 */
public class ItemDto {

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
	 * Instantiates a new item dto.
	 *
	 * @param obj
	 *            the obj
	 */
	public ItemDto(JSONObject obj) {
		super();
		this.owner = obj.getString("owner");
		this.title = obj.getString("title");
		this.description = obj.getString("description");
	}

	/**
	 * To json.
	 *
	 * @return the JSON object
	 */
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("owner", getOwner());
		obj.put("title", getTitle());
		obj.put("description", getDescription());
		return obj;
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
