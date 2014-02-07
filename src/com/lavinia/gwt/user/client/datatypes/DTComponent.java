/*
 * Created on 09/08/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.client.datatypes;
import java.util.Collection;
import java.util.Vector;

import com.google.gwt.user.client.rpc.IsSerializable;
/**
 * @author Cecilia Techera
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DTComponent implements IsSerializable{
	public String name;	
	public String autor;
	public String description;
	public String pack;
	
	/**
	   * This field is a Set that must always contain DTParameter.
	   * 
	   * @gwt.typeArgs <com.lavinia.gwt.user.client.datatypes.DTParameter>
	   */
	public Collection parameters;
	/**
	   * This field is a Set that must always contain DTType.
	   * 
	   * @gwt.typeArgs <com.lavinia.gwt.user.client.datatypes.DTType>
	   */
	public Collection types;
	/**
	   * This field is a Set that must always contain DTResource.
	   * 
	   * @gwt.typeArgs <com.lavinia.gwt.user.client.datatypes.DTResource>
	   */
	public Collection resources;
	/**
	 * 
	 */
	public DTComponent() {
		super();
		name="";
		autor="";
		description="";
		pack="";
		parameters=new Vector();
		types=new Vector();
		resources=new Vector();
	}
	/**
	 * @param name	
	 * @param autor
	 * @param description
	 * @param pack	 
	 * @param parameters
	 * @param types
	 * @param resources
	 */
	public DTComponent(String name, String autor,
			String description, String fileImp,
			Collection parameters, Collection types,
			Collection resources) {
		super();
		this.name = name;		
		this.autor = autor;
		this.description = description;
		this.pack = fileImp;		
		this.parameters = parameters;
		this.types = types;
		this.resources=resources;
	}
	/**
	 * @return Returns the autor.
	 */
	public String getAutor() {
		return autor;
	}
	/**
	 * @param autor The autor to set.
	 */
	public void setAutor(String autor) {
		this.autor = autor;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the pack.
	 */
	public String getPack() {
		return pack;
	}
	/**
	 * @param fileImp The pack to set.
	 */
	public void setPack(String pack) {
		this.pack = pack;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return Returns the parameters.
	 */
	public Collection getParameters() {
		return parameters;
	}
	/**
	 * @param parameters The parameters to set.
	 */
	public void setParameters(Collection parameters) {
		this.parameters = parameters;
	}
	/**
	 * @return Returns the types.
	 */
	public Collection getTypes() {
		return types;
	}
	/**
	 * @param types The types to set.
	 */
	public void setTypes(Collection types) {
		this.types = types;
	}
	/**
	 * @return Returns the resources.
	 */
	public Collection getResources() {
		return resources;
	}
	/**
	 * @param resources The resources to set.
	 */
	public void setResources(Collection resources) {
		this.resources = resources;
	}

}

