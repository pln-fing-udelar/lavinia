package com.lavinia.gwt.user.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jboss.util.file.Files;

public class FileDownloadServlet extends HttpServlet{
	
	
	static Logger log = Logger.getLogger(FileDownloadServlet.class.getName());
		
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException{

		try{
			
			String id = request.getParameter("id");
			AppConfiguration apconf = AppConfiguration.getInstance();
			
			String fileName = id+".xml";
			File f = new File (apconf.getEnginesXMLsPath()+"/"+fileName);
			FileInputStream fileToDownload = new FileInputStream(f);
			ServletOutputStream out = response.getOutputStream();
			
			response.setContentType("application/xml");
			
			response.setHeader("Content-Disposition","attachment; filename="+"LaviniaAE.xml");
			
			response.setContentLength(fileToDownload.available());
			int c;
			
			while((c=fileToDownload.read()) != -1){
				out.write(c);
			}
			out.flush();
			out.close();
			
			
			fileToDownload.close();
		
			
			
			Files.delete(f);
			
		}catch(IOException e){
			String msg = "Error de I/O al guardar descriptor XML de AE";
			if (AppConfiguration.getInstance().getLang().equals("es"))
				msg = "I/O error saving XML descriptor of AnalysisEngine";
			log.error(msg);
			
			throw new ServletException();
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException {
		processRequest(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException{
		processRequest(request, response);
	}
	
}
