package com.lavinia.gwt.user.server;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;


public class ParamsUploadServlet extends HttpServlet{ 

	static Logger log = Logger.getLogger(ParamsUploadServlet.class.getName());

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException{ 
	   	
	
   		AppConfiguration apconf = AppConfiguration.getInstance();
		String path = apconf.getParams();
		String barra ="/";
   		
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List items = null;
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e) {
			String msg = "Error al obtener items de request";
			if (apconf.getLang().equals("es"))
				msg = "Error obtaining request items";
			log.error(msg);
			throw new ServletException();
		}
		
		Iterator iter = items.iterator();		
		
		String folder=request.getParameter("id");
		String base = path+barra+folder;
		
		File fold=null;        		
		
		
		while (iter.hasNext()) { 
			
	   		FileItem item = (FileItem) iter.next(); 
	   
	   		if (!item.isFormField()) {
	   			if (fold==null){
	   				fold = new File(base);
	   				fold.mkdir();
	   			}
	   			String fileName = item.getName();
	   			if (fileName != null && !fileName.equals("")) {	   				
	   				String field = item.getFieldName();
	   				int ind1 = field.indexOf("%");	
	   				if (ind1>=0){
	   					
   						String component = field.substring(0,ind1);
   						
   						int ind2 = field.lastIndexOf("%");
   						if (ind2>=0){
   							
			   				String param = field.substring(ind1+1,ind2);
			   				
			   				String index = field.substring(ind2+1, field.length());
			        	 
			        		fileName = FilenameUtils.getName(fileName);
			        		
			        		File dircomp = new File(base+barra+component+"."+index);
			        		if (!dircomp.exists())
			        			dircomp.mkdir();
			        		File dirparam = new File(base+barra+component+"."+index+barra+param);
			        		if (!dirparam.exists())
			        			dirparam.mkdir();
			        		        		
			        		File uploadedFile = new File(base+barra+component+"."+index+barra+param+barra+fileName);	        		
			        		
			        		try {
								item.write(uploadedFile);
							} catch (Exception e) {
								String msg = "Error al escribir archivo "+base+barra+component+"."+index+barra+param+barra+fileName;
								if (apconf.getLang().equals("es"))
									msg = "Error writing file "+base+barra+component+"."+index+barra+param+barra+fileName;
								log.error(msg);
								
								throw new ServletException();
							}
   						}
	   					
	   				}
				}       	                               
	        }
	   }		   
   }
}