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


public class CorpusUploadServlet extends HttpServlet{ 

	static Logger log = Logger.getLogger(CorpusUploadServlet.class.getName());

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException{ 
	   	  
   		AppConfiguration apconf = AppConfiguration.getInstance();
		String path = apconf.getCorpus();
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
		
		String folder="folder"+request.getParameter("id");
		File fold = new File(path+barra+folder);
		fold.mkdir();        		
		
		
		while (iter.hasNext()) { 
			
	   		FileItem item = (FileItem) iter.next(); 
	   
	   		if (!item.isFormField()) {	   		
	   			String fileName = item.getName();
	   			
	        	if (fileName != null && !fileName.equals("")) { 
	        		fileName = FilenameUtils.getName(fileName);	        		
	        		File uploadedFile = new File(path+barra+folder+barra+fileName);	        		
	        		try {
						item.write(uploadedFile);
						
					} catch (Exception e) {
						String msg = "Error al escribir archivo "+path+barra+folder+barra+fileName;
						if (apconf.getLang().equals("es"))
							msg = "Error writing file "+path+barra+folder+barra+fileName;
						log.error(msg);			
						
						throw new ServletException();
					}			
				}        	                               
	        }
	   }		   
   }
}