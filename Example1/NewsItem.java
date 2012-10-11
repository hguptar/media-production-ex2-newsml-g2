package aalto.media.newsml;

/**
 * NewsItem class for storing all relevant information of the news item
 *
 */
public class NewsItem {

	// Unique id of the news item
	private String guid;
	// News item version if modiefied and re-send
	private String version;
	// Date and time when the latest version was sent to customer
	private String version_created;
	// Type of the news item (Pääjuttu, Kainalo, Tausta...)
	private String type_role;
	// Related categories of the news item
	private String[] categories;
	
	public NewsItem() {};
	
	public NewsItem(String guid, String version, String version_created,
			String type_role, String[] categories) {
		super();
		this.guid = guid;
		this.version = version;
		this.version_created = version_created;
		this.type_role = type_role;
		this.categories = categories;
	}
	
	/*
	 * Getters and Setter for NewsItem class
	 * 
	 */

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVersion_created() {
		return version_created;
	}

	public void setVersion_created(String version_created) {
		this.version_created = version_created;
	}

	public String getType_role() {
		return type_role;
	}

	public void setType_role(String type_role) {
		this.type_role = type_role;
	}

	public String[] getCategories() {
		return categories;
	}

	public void setCategories(String[] categories) {
		this.categories = categories;
	}
	
	/*
	 * String presentation of the news item.
	 */
	
	@Override
	public String toString() {
		String s = "NewsItem " + getGuid() + "\n" +
				"Version " + getVersion() + ", sent " + getVersion_created();
		return s;
	}
}
