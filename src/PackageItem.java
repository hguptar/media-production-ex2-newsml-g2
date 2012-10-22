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
     * Fields in packageItem element
     */
    private String guid;
    private String version;
	
	/* 
	 * Constant fields in itemMeta element
	 */
    private ItemMetaPackage itemMeta;
	

	/*
	 * Fields in contentMeta element
	 */
	private ContentMetaPackage contentMeta;
	
	/*
	 * Fields in groupSet element
	 */
	private String root;
	private ArrayList<GroupItem> groupItems;
	
	public PackageItem() {
		groupItems = new ArrayList<GroupItem>();
		itemMeta = new ItemMetaPackage();
		contentMeta = new ContentMetaPackage();
	}	
	
	/*
     * Getters and setters for packageItem element
     */
	
	public ItemMetaPackage getItemMeta() {
		return this.itemMeta;
	}
	
	public ContentMetaPackage getContentMeta() {
		return this.contentMeta;
	}
	
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
	
	public String getRoot() {
		return this.root;
	}
	
	public void setRoot(String root) {
		this.root = root;
	}
	
	public ArrayList<GroupItem> getGroupItems() {
		return this.groupItems;
	}
    
	/*
	 * Method for adding news items into package
	 */
	
	public String addNewGroup(String groupId, String role, String groupRef) {
		GroupItem group = new GroupItem();
	
		if(groupId.equals("")) {
			group.setId("G"+Integer.toString(this.groupItems.size()+1));
		} else {
			group.setId(groupId);
		}
		
		if(!groupRef.equals("")) {
			GroupRef group_ref = new GroupRef();
			group_ref.setId(group.getId());
			getGroupById(groupRef).setGroupRef(group_ref);
		}
		
	    group.setRole(role);
	    
	    this.groupItems.add(group);
	    
	    return group.getId();
	}
		
	public GroupItem getGroupById(String id) {
		for (GroupItem group : this.groupItems) {
			if(group.getId().equals(id)) {
				return group;
			}
		}
		
		return null;
	}

	public void addNewsItem(NewsItem newsItem, String groupId) {
		GroupItem group;
		
		if(groupId.equals("")) {
			group = new GroupItem();
			group.setId("G"+Integer.toString(this.groupItems.size()+1));
		    group.setRole("group:package");
		} else {
			group = getGroupById(groupId);
		}
		
	    ItemRef item_ref = new ItemRef();
	    
	    item_ref.setResidref(newsItem.getGuid());
	    item_ref.setSize(Long.toString(newsItem.getSize()));
	    item_ref.setItemClass(newsItem.getItemMeta().getItemClass());
	    item_ref.setProvider(newsItem.getItemMeta().getProvider());
	    item_ref.setVersion_created(newsItem.getContentMeta().getContentCreated());
	    item_ref.setPubStatus(newsItem.getItemMeta().getPubStatus());
	    item_ref.setHeadline(newsItem.getContentMeta().getHeadline());
	    item_ref.setDescription(newsItem.getContentMeta().getDescription());
	    
	    group.setItemRef(item_ref);
	    
	    if(groupId.equals("")) {
	    	this.groupItems.add(group);
	    }
    }

	public class GroupItem {
	    // group item attributes
		private String id;
		private String role;
		private final String mode = "pgrmode:seq";
		private ArrayList<GroupRef> group_refs;
		private ArrayList<ItemRef> item_refs;

		public GroupItem() {
			group_refs = new ArrayList<GroupRef>();
			item_refs = new ArrayList<ItemRef>();
		}
		
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
		
		public void setMode(String mode) {}
		
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
	
	public class GroupRef {
	    private String id;
	    
	    public GroupRef() {};
	    
	    public String getId() {
	        return this.id;
	    }
	    
	    public void setId(String id) {
	        this.id = id;
	    }
	}
	
	public class ItemRef {
	    
        //  itemRef attributes
	    private String residref;
	    private final String content_type = "application/vnd.iptc.g2.newsitem+xml";
	    private String size;
	    // itemRef child elements
	    private String item_class;
	    private String provider;
	    private String version_created;
	    private String pub_status;
	    private String headline;
	    private String description;
	    
	    public ItemRef() {};
	    
	    public String getResidref() {
	        return this.residref;
	    }
	    
	    public void setResidref(String residref) {
	        this.residref = residref;
	    }
	    
	    public String getContentType() {
	    	return this.content_type;
	    }
	    
	    public String getSize() {
            return this.size;
        }
        
        public void setSize(String size) {
            this.size = size;
        }
	    
	    public String getProvider() {
	        return this.provider;
	    }
	    
	    public void setProvider(String provider) {
	        this.provider = provider;
	    }
	    
	    public String getItemClass() {
	        return this.item_class;
	    }
	    
	    public void setItemClass(String item_class) {
	        this.item_class = item_class;
	    }
	    
	    public String getVersion_created() {
            return version_created;
        }

        public void setVersion_created(String version_created) {
            this.version_created = version_created;
        }

        public String getPubStatus() {
            return pub_status;
        }

        public void setPubStatus(String pub_status) {
            this.pub_status = pub_status;
        }

        public String getHeadline() {
            return headline;
        }

        public void setHeadline(String headline) {
            this.headline = headline;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

	}

}
