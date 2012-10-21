
public class ContentMetaPackage {

	private Contributor contributor;
	private Headline headline;
	
	public ContentMetaPackage() {
		contributor = new Contributor();
		headline = new Headline();
	};
	
	public Contributor getContributor() {
		return this.contributor;
	}
	
	public Headline getHeadline() {
		return this.headline;
	}
	
 	public class Contributor {
		private String jobtitle;
		private String qcode;
		private String job_name;
		private String name;
		private String def_validto;
		private String def_text;
		private String note_validto;
		private String note_text;
		
		public Contributor() {}

		public String getJobtitle() {
			return jobtitle;
		}

		public void setJobtitle(String jobtitle) {
			this.jobtitle = jobtitle;
		}

		public String getQcode() {
			return qcode;
		}

		public void setQcode(String qcode) {
			this.qcode = qcode;
		}

		public String getJob_name() {
			return job_name;
		}

		public void setJob_name(String job_name) {
			this.job_name = job_name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDef_validto() {
			return def_validto;
		}

		public void setDef_validto(String def_validto) {
			this.def_validto = def_validto;
		}

		public String getDef_text() {
			return def_text;
		}

		public void setDef_text(String def_text) {
			this.def_text = def_text;
		}

		public String getNote_validto() {
			return note_validto;
		}

		public void setNote_validto(String note_validto) {
			this.note_validto = note_validto;
		}

		public String getNote_text() {
			return note_text;
		}

		public void setNote_text(String note_text) {
			this.note_text = note_text;
		};
		
	}
	
	public class Headline {
		private String headline_lang;
		private String headline_text;
		
		public Headline() {}

		public String getHeadline_lang() {
			return headline_lang;
		}

		public void setHeadline_lang(String headline_lang) {
			this.headline_lang = headline_lang;
		}

		public String getHeadline_text() {
			return headline_text;
		}

		public void setHeadline_text(String headline_text) {
			this.headline_text = headline_text;
		};
		
		
	}
}
