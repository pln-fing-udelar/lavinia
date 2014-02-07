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

public class ResourceUploadServlet extends HttpServlet {

	static Logger log = Logger.getLogger(ResourceUploadServlet.class.getName());

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {

		AppConfiguration apconf = AppConfiguration.getInstance();
		String path = apconf.getLocalResourcesPath() + "/";

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

		String component = request.getParameter("name");

		if (items.size() > 0) {
			File dir = new File(path + component);
			if (!dir.exists())
				dir.mkdir();
		}

		while (iter.hasNext()) {
			FileItem item = (FileItem) iter.next();
			if (!item.isFormField()) {

				String fileName = item.getName();
				if (fileName != null && !fileName.equals("")) {
					fileName = FilenameUtils.getName(fileName);
					File uploadedFile = new File(path + "/" + component + "/"
							+ fileName);
					try {
						item.write(uploadedFile);
					} catch (Exception e) {
						String msg = "Error al escribir archivo " + path + "/"
						+ component + "/" + fileName;
						if (apconf.getLang().equals("es"))
							msg = "Error writing file " + path + "/"
							+ component + "/" + fileName;
						log.error(msg);	
						
						throw new ServletException();
					}
				}
			}
		}
	}
}
