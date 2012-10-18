
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * NewsItem class for storing all relevant information of the news item
 *
 */
public class NewsItem {

	// Unique id of the news item
	private String guid;
	// News item version if modified and re-sent
	private String version;
	// Date and time when the latest version was sent to customer
	private String version_created;
	private Date version_created_date;
	// Type of the news item (Paajuttu, Kainalo, Tausta...)
	private String type_role;
	// Department
	private String department;
	// Related categories of the news item
	private String[] categories;
	// Urgency of the news item
	private String urgency;
	// Name of the service of the news item
	private String name;
	// Location of the news item
	private String location;
	// Class of the news item
	private String class_ni;
	// Headline of the news item
	private String headline;

	// Add more fields here.
	
	public NewsItem() {};
	
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

	public String getVersionCreated() {
		return version_created;
	}

	public void setVersionCreated(String version_created) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try {
			Date date = df.parse(version_created);
			setVersionCreatedDate(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.version_created = version_created;
	}

	public Date getVersionCreatedDate() {
		return version_created_date;
	}

	public void setVersionCreatedDate(Date version_created_date) {
		this.version_created_date = version_created_date;
	}

	public String getTypeRole() {
		return type_role;
	}

	public void setTypeRole(String type_role) {
		this.type_role = type_role;
	}	

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
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
	
	public String getUrgency() {
	    return urgency;
	}
	
	public void setUrgency(String urgency) {
	    this.urgency = urgency;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getClassNI() {
		return class_ni;
	}
	
	public void setClassNI(String class_ni) {
		this.class_ni = class_ni;
	}
	
	public String getHeadline() {
		return headline;
	}
	
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	
	@Override
	public String toString() {
		String s = "NewsItem " + getGuid() + "\n" +
				"Version " + getVersion() + ", sent " + getVersionCreated();
		return s;
	}
}
