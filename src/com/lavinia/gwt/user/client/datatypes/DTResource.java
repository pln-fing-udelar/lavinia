package com.lavinia.gwt.user.client.datatypes;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DTResource implements IsSerializable {
		private String name;		
		private String description;
		private String inter;
		private String impl;
		
		private boolean global;
		private String filename;
		private boolean nuevo;
		
		public DTResource(){
			super();
		}


		public String getDescription() {
			return description;
		}


		public void setDescription(String description) {
			this.description = description;
		}


		public String getImpl() {
			return impl;
		}


		public void setImpl(String impl) {
			this.impl = impl;
		}


		public String getInter() {
			return inter;
		}


		public void setInter(String inter) {
			this.inter = inter;
		}


		public String getName() {
			return name;
		}


		public void setName(String name) {
			this.name = name;
		}


		public DTResource(String name, String description, String inter, String impl, String filename, boolean global,boolean nuevo) {
			super();
			this.name = name;
			this.description = description;
			this.inter = inter;
			this.impl = impl;			
			this.global = global;
			this.filename = filename;
			this.nuevo=nuevo;
		}


		public boolean isGlobal() {
			return global;
		}


		public void setGlobal(boolean global) {
			this.global = global;
		}


		public String getFilename() {
			return filename;
		}


		public void setFilename(String filename) {
			this.filename = filename;
		}


		public boolean isNuevo() {
			return nuevo;
		}


		public void setNuevo(boolean nuevo) {
			this.nuevo = nuevo;
		}


		
		
}
