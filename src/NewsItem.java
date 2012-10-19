
/**
 * NewsItem class for storing all relevant information of the news item
 *
 */
public class NewsItem {

	// Unique id of the news item
	private String guid;
	// News item version if modified and re-sent
	private String version;
	private String catalog_ref;
	// See ItemMeta.java
	private ItemMeta item_meta = new ItemMeta();
   // See ContentMeta.java
	private ContentMeta content_meta = new ContentMeta();
	//Size of a news item
	private long size;
	
	public NewsItem() {};
	
	/*
	 * Getters and Setter for NewsItem class
	 * 
	 */
	public long getSize() {
		return size;
	}
	
	public void setSize(long size) {
		this.size = size;
	}
	
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
	
	public String getCatalogRef() {
	    return this.catalog_ref;
	}
	
	public void setCatalogRef(String catalog_ref) {
	    this.catalog_ref = catalog_ref;
	}

	public ItemMeta getItemMeta() {
	    return this.item_meta;
	}
	
	public void setItemMeta(ItemMeta item_meta) {
	    this.item_meta = item_meta;
	}
	
	public ContentMeta getContentMeta() {
        return this.content_meta;
    }
    
    public void setContentMeta(ContentMeta content_meta) {
        this.content_meta = content_meta;
    }

	
	/*
	 * String presentation of the news item.
	 */
    
	@Override
	public String toString() {
		String s = "NewsItem " + getGuid() + "\n" + "Version " + getVersion() + ", sent " + this.getItemMeta().getVersionCreated();
		return s;
	}
}
