import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ItemMeta {
    
    // Class of the news item
    private String item_class;
    private String provider;
    // Date and time when the latest version was sent to customer
    private String version_created;
    private Date version_created_date;
    // Date and time when the news item was first created
    private String first_created;
    private Date first_created_date;
    private String pub_status;
    // Type of the news item (Paajuttu, Kainalo, Tausta...)
    private String role;
    private String service_code;
    private String service_name;
    // PackageItem specific elements
    private String generator;
    private String title;
    
    public ItemMeta() {};
    
    public String getItemClass() {
        return item_class;
    }
    
    public void setItemClass(String item_class) {
        this.item_class = item_class;
    }
    
    public String getProvider() {
        return this.provider;
    }
    
    public void setProvider(String provider) {
        this.provider = provider;
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
    
    public String getFirstCreated() {
        return first_created;
    }

    public void setFirstCreated(String first_created) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date = df.parse(first_created);
            setVersionCreatedDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.first_created = first_created;
    }

    public Date getFirstCreatedDate() {
        return first_created_date;
    }

    public void setFirstCreatedDate(Date first_created_date) {
        this.first_created_date = first_created_date;
    }

    public String getPubStatus() {
        return this.pub_status;
    }
    
    public void setPubStatus(String pub_status) {
        this.pub_status = pub_status;
    }
    
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }   
    
    public String getServiceCode() {
        return this.service_code;
    }
    
    public void setServiceCode(String service_code) {
        this.service_code = service_code;
    }
    
    public String getServiceName() {
        return this.service_name;
    }
    
    public void setServiceName(String service_name) {
        this.service_name = service_name;
    }
    
    public String getGenerator() {
        return this.generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
}
