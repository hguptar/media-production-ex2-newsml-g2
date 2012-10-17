import java.util.ArrayList;

public class PackageItem {
	
	
	/* 
	 * Constant fields in packageItem element
	 */
	private static final String STANDARD = "NewsML-G2";
	private static final String STANDARD_VERSION = "2.8";
	private static final String CONFORMANCE = "power";
	private static final String XMLNS = "http://iptc.org/std/nar/2006-10-01/";
	private static final String XMLNS_XSI = "http://www.w3.org/2001/XMLSchema-instance";
	private static final String XSI_SCHEMALOCATION = "http://iptc.org/std/nar/2006-10-01/" +
			"../specification/NewsML-G2_2.9-spec-All-Power.xsd";
	private static final String CATALOGREF = "http://www.iptc.org/std/catalog/catalog.IPTC-G2-standards_16.xml";
	
	/* 
	 * Constant fields in itemMeta element
	 */
	private static final String ITEMCLASS = "ninat:composite";
	private static final String PROVIDER = "STT";
	private static final String GENERATOR = "NewsML Package Generator of Group 3";
	
	/*
	 * Fields in itemMeta element
	 */
	private String guid;
	private String version_created;
	private String version_created_date;
	private String service_name;
	private String title;
	
	//...
	
	/*
	 * Fields in contentMetadata element
	 */
	private String headline;
	private String contributor;
	
	/*
	 * Fields in groupSet element
	 */
	private ArrayList<GroupItem> groupItems;
	
	public PackageItem() {
		groupItems = new ArrayList<GroupItem>();
	}	
	
	/*
	 * Getters and setters
	 */
	public String getHeadline() {
	    return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }	
	
    public String getContributorName() {
        return contributor;
    }

    public void setContributorName(String contributor) {
        this.contributor = contributor;
    }
    
	/*
	 * Method for adding news items into package
	 */

	public void addNewsItem(NewsItem newsItem) {
		// Implement a mechanism for adding new newsitems
		// use some kind of id generation system
		
		
	}

	private class GroupItem {
		private String id;
		// Add more fields here. See example packageItem.
		//...

		public GroupItem() {};
		
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
	}

}
