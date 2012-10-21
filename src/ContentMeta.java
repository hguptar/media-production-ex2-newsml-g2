import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ContentMeta {

        // Urgency of the news item
        private String urgency;
        // Content created
        private String content_created;
        private Date content_created_date;
        // Content modified
        private String content_modified;
        private Date content_modified_date;
        private String located;
        public Subject subject;
        private String by;
        private String description;
        // Headline of the news item
        private String headline;
        // packageItem specific elemets
        private String contributor_name;
        private String contributor_definition;
        
        public ContentMeta() {
            this.subject = new Subject();
        };
        
        public String getUrgency() {
            return urgency;
        }
        
        public void setUrgency(String urgency) {
            this.urgency = urgency;
        }
        
        public String getContentCreated() {
            return this.content_created;
        }
        
        public void setContentCreated(String content_created) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                Date date = df.parse(content_created);
                setContentCreatedDate(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            this.content_created = content_created;
        }
        
        public Date getContentCreatedDate() {
            return this.content_created_date;
        }
        
        public void setContentCreatedDate(Date date) {
            this.content_created_date = date;
        }
         
        public String getContentModified() {
            return this.content_modified;
        }
        
        public void setContentModified(String content_modified) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                Date date = df.parse(content_modified);
                setContentModifiedDate(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            this.content_modified = content_modified;
        }
        
        public Date getContentModifiedDate() {
            return this.content_modified_date;
        }
        
        public void setContentModifiedDate(Date date) {
            this.content_modified_date = date;
        }
        
        public String getLocation() {
            return located;
        }
        
        public void setLocation(String location) {
            this.located = location;
        }
        
        public Subject getSubject() {
            return this.subject;
        }
        
        public void setSubject(Subject subject) {
            this.subject = subject;
        }
        
        public String getBy() {
            return this.by;
        }
        
        public void setBy(String by) {
            this.by = by;
        }
        
        public String getDescription() {
            return description;
        }
    
        public void setDescription(String description) {
            this.description = description;
        }
        
        public String getHeadline() {
            return headline;
        }
        
        public void setHeadline(String headline) {
            this.headline = headline;
        }
        
        public String getContributorName() {
            return contributor_name;
        }
    
        public void setContributorName(String contributor_name) {
            this.contributor_name = contributor_name;
        }
    
        public String getContributorDefinition() {
            return contributor_definition;
        }
    
        public void setContributorDefinition(String contributor_definition) {
            this.contributor_definition = contributor_definition;
        }
        
        /*
         * Subclass representing <subject> element in contentMeta
         * 
         */
        public class Subject {
            
            private String type;
            private String code;
            private String name;
            private String topic;
            private String department;
            private ArrayList<String> categories;
            
            public Subject() {};
            
            public String getType() {
                return this.type;
            }
            
            public void setType(String type) {
                this.type = type;
            }
            
            public String getCode() {
                return this.code;
            }
            
            public void setCode(String code) {
                this.code = code;
            }
            
            public String getName() {
                return this.name;
            }
            
            public void setName(String name) {
                this.name = name;
            }
            
            public String getTopic() {
                return this.topic;
            }
            
            public void setTopic(String topic) {
                this.topic = topic;
            }
            
            public String getDepartment() {
                return this.department;
            }
            
            public void setDepartment(String department) {
                this.department = department;
            }
            
            public ArrayList<String> getCategories() {
                return this.categories;
            }
            
            public void setCategories(ArrayList<String> categories) {
                this.categories = categories;
            }
           
        }
}
