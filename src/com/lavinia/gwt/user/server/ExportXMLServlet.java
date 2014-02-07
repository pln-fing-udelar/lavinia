package com.lavinia.gwt.user.server;

import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jboss.util.file.Files;

public class ExportXMLServlet  extends HttpServlet{
	
	static Logger log = Logger.getLogger(ExportXMLServlet.class.getName());
		
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException {
	
	
			String id = request.getParameter("id");
			AppConfiguration apconf = AppConfiguration.getInstance();
			
			String fileName = id+".xml";
	
		try{
			FileInputStream fileToDownload = new FileInputStream(apconf.getEnginesXMLsPath()+"/"+fileName);
			ServletOutputStream out = response.getOutputStream();
			
			response.setContentType("application/xml");
			
			response.setHeader("Content-Disposition","charset=UTF-8 attachment; filename="+"XmiResult.xml");
			
			response.setContentLength(fileToDownload.available());
			
			response.setCharacterEncoding("UTF-8");
			
			int c;		
			
			while((c=fileToDownload.read()) != -1){
				out.write(c);
			}
			
			out.flush();
			out.close();			
			
			fileToDownload.close();			
			
			
			
		}catch(IOException e){
			String msg = "Error de I/O al guardar XML con resultados";
			if (apconf.getLang().equals("es"))
				msg = "I/O error saving XML file with results";
			log.error(msg);
			
			throw new ServletException();
		}finally{
			Files.delete(apconf.getEnginesXMLsPath()+"/"+fileName);
		}
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException {
		processRequest(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException {
		processRequest(request, response);
	}
	
}


