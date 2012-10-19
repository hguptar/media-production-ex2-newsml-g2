import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
     * Fields in packageItem element
     */
    private String guid;
    private String version;
	
	/* 
	 * Constant fields in itemMeta element
	 */
	private static final String ITEMCLASS = "ninat:composite";
	private static final String PROVIDER = "STT";
	private static final String GENERATOR = "NewsML Package Generator of Group 3";
	
	/*
	 * Fields in itemMeta element (ignored firstCreated, pubStatus, profile, edNote, signal and link) 
	 */
	private String version_created;
	private Date version_created_date;
	private String service_name;
	private String title;
	
	/*
	 * Fields in contentMeta element
	 */
	private String contributor;
	private String contributor_definition;
	private String headline;
	
	/*
	 * Fields in groupSet element
	 */
	private ArrayList<GroupItem> groupItems;
	
	public PackageItem() {
		groupItems = new ArrayList<GroupItem>();
		setPackageMetaData();
	}	
	
	/*
     * Getters and setters for packageItem element
     */
	
	public String getStandard() {
	    return PackageItem.STANDARD;
	}
	
	public String getStandardVersion() {
	    return PackageItem.STANDARD_VERSION;
	}
	
	public String getConformance() {
	    return PackageItem.CONFORMANCE;
	}
	
	public String getXlmns() {
	    return PackageItem.XMLNS;
	}
	
	public String getXlmnsXsi() {
	    return PackageItem.XMLNS_XSI;
	}
	
	public String getXsiSchemaLocation() {
	    return PackageItem.XSI_SCHEMALOCATION;
	}
	
	public String getCatalogRef() {
	    return PackageItem.CATALOGREF;
	}
	
	public String getGuid() {
	    return this.guid;
	}
	
	public void setGuid(String guid) {
	    this.guid = guid;
	}
	
	public String getVersion() {
	    return this.version;
	}
	
	public void setVersion(String version) {
	    this.version = version;
	}
	
	
	/*
	 * Getters and setters for itemMeta element
	 */
	
	public String getItemClass() {
	    return PackageItem.ITEMCLASS;
	}
	
	public String getProvider() {
	    return PackageItem.PROVIDER;
	}
	
	public String getGenerator() {
	    return PackageItem.GENERATOR;
	}
	
	public String getVersionCreated() {
	    return this.version_created;
	}
	
	public void setVersionCreated(String version_created) {
	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'H:m:s");
	    try {
	        Date date = df.parse(version_created);
	        setVersionCreatedDate(date);
	    } catch (ParseException e) {
            e.printStackTrace();
        }
	    this.version_created = version_created;
	}
	
	public Date getVersionCreatedDate() {
	    return this.version_created_date;
	}
	
	public void setVersionCreatedDate(Date date) {
	    this.version_created_date = date;
	}
	
	public String getServiceName() {
	    return this.service_name;
	}
	
	public void setServiceName(String service_name) {
	    this.service_name = service_name;
	}
	
	public String getTitle() {
	    return this.title;
	}
	
	public void setTitle(String title) {
	    this.title = title;
	}
	
	/*
     * Getters and setters for contentMeta element
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
    
    public String getContributorDefinition() {
        return contributor_definition;
    }
    
    public void setContributorDefinition(String definition) {
        this.contributor_definition = definition;
    }
    
    /*
     * Method for setting package meta data (should be queried from the user)
     */
    public void setPackageMetaData() {
        this.setGuid("777");
        this.setVersion("1.0");
        this.setHeadline("Example headline");
        this.setContributorName("T-75.4210 - Media Production and Use Processes: Group 3");
        // Fields in itemMeta element
        this.setVersionCreated(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date()));
        this.setServiceName("");
        this.setTitle("");
        // Fields in contentMeta element
        this.setContributorName("");
        this.setContributorDefinition("");
        this.setHeadline("");
    }
    
	/*
	 * Method for adding news items into package
	 */

	public void addNewsItem(NewsItem newsItem) {
		// Implement a mechanism for adding new news items
		// use some kind of id generation system
	}

	private class GroupItem {
	    // group item attributes
		private String id;
		private String role;
		private String mode;
		private ArrayList<GroupRef> group_refs;
		private ArrayList<ItemRef> item_refs;

		public GroupItem() {};
		
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
		
		public String getRole() {
		    return this.role;
		}
		
		public void setRole(String role) {
		    this.role = role;
		}
		
		public String getMode() {
		    return this.mode;
		}
		
		public void setMode(String mode) {
		    this.mode = mode;
		}
		
		public ArrayList<GroupRef> getGroupRef() {
		    return this.group_refs;
		}
		
		public void setGroupRef(GroupRef group_ref) {
		    this.group_refs.add(group_ref);
		}
		
		public ArrayList<ItemRef> getItemRef() {
		    return this.item_refs;
		}
		
		public void setItemRef(ItemRef item_ref) {
		    this.item_refs.add(item_ref);
		}
	}
	
	private class GroupRef {
	    private String id;
	    
	    public GroupRef() {};
	    
	    public String getId() {
	        return this.id;
	    }
	    
	    public void setId(String id) {
	        this.id = id;
	    }
	}
	
	private class ItemRef {
	    //  itemRef attributes
	    private String residref;
	    private String content_type;
	    private String size;
	    // itemRef child elements
	    private String item_class;
	    private String provider;
	    private String version_created;
	    private String version_created_date;
	    private String pub_status;
	    private String headline;
	    private String description;
	    
	    public ItemRef() {};
	    
	    public String getProvider() {
	        return this.provider;
	    }
	    
	    public void setProvider(String provider) {
	        this.provider = provider;
	    }
	}

}
