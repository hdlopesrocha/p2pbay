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
public class PurchaseDto implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7647175142472873433L;

	/** The owner. */
	private float offer;

	/** The title. */
	private String title;

	/** The description. */
	private String description;

	private String id;

	/**
	 * Instantiates a new item dto.
	 *
	 * @param title
	 *            the title
	 * @param description
	 *            the description
	 * @param offer
	 *            the offer
	 */
	public PurchaseDto(String id,String title, String description, float offer) {
		super();
		this.id = id;
		this.offer = offer;
		this.title = title;
		this.description = description;
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

	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}

}
