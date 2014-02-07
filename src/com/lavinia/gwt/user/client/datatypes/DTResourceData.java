package com.lavinia.gwt.user.client.datatypes;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DTResourceData implements IsSerializable {
		private String name;		
		private String description;
		private String url;	
		private String externo;
		
		public DTResourceData(){
			super();
		}


		public String getDescription() {
			return description;
		}


		public void setDescription(String description) {
			this.description = description;
		}


		public String getURL() {
			return url;
		}


		public void setUrl(String url) {
			this.url = url;
		}


		public String getName() {
			return name;
		}


		public void setName(String name) {
			this.name = name;
		}


		public DTResourceData(String name, String description, String url, String ext) {
			super();
			this.name = name;
			this.description = description;
			this.url = url;
			this.externo=ext;
						
		}


		public String isExterno() {
			return externo;
		}


		public void setExterno(String ext) {
			this.externo = ext;
		}


		
}
