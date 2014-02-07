package com.lavinia.gwt.user.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import au.id.jericho.lib.html.HTMLElementName;
import au.id.jericho.lib.html.Segment;
import au.id.jericho.lib.html.Source;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.lavinia.gwt.user.client.datatypes.DTResourceData;
import com.lavinia.gwt.user.client.exceptions.ProcessingException;
import com.lavinia.gwt.user.client.exceptions.XMLErrorException;
import com.lavinia.gwt.user.client.interfaces.IResources;
import com.lavinia.gwt.user.client.interfaces.IUtils;

public class IUtilsImp extends RemoteServiceServlet implements IUtils {

	private Namespace NS = Namespace
			.getNamespace("http://uima.apache.org/resourceSpecifier");

	static Logger log = Logger.getLogger(IUtilsImp.class.getName());

	/*
	 * Obtiene la ruta de instalacion de la aplicacion y verifica si es la misma
	 * de siempre. Si la ruta cambio, actualiza la misma en la lista de recursos
	 * y actualiza las rutas a recursos que tienen los descriptores de los
	 * componentes para que sean correctas
	 */
	public Boolean configure()throws Exception {

		String newpath = getServletContext().getRealPath("/");
		//System.out.println("RUTA: "+newpath);
		
		newpath = newpath.replace("\\", "/");
		if (!newpath.startsWith("/"))
			newpath = "/" + newpath;

		AppConfiguration.setRealPath(newpath);

		String oldpath = AppConfiguration.getInstance().getPath("LAVINIA_PATH");

		if (!oldpath.equals(newpath)) {
			if (AppConfiguration.getInstance().getLang().equals("es"))
				System.out.println("Actualizando ruta a Lavinia...");
			else
				System.out.println("Updating Lavinia path...");
			IResources ir = Factory.getInstance().getIResources();
			try {
				ir.updateLaviniaPath(new DTResourceData("LAVINIA_PATH", "",
						newpath, "false"));
			} catch (Exception e) {
				String msg = "Error al actualizar LAVINIA PATH";
				if (!AppConfiguration.getInstance().getLang().equals("es")){
					msg = "Error updating LAVINIA PATH";
				}
				log.error(msg);
							
				throw e;
			}
			updateComponentsPaths(newpath, oldpath);
			return true;
		}

		PropertyConfigurator.configure(AppConfiguration.getInstance()
				.getResources()
				+ "/log4j.properties");


		return false;
	}
	
	public void setLanguage(String lang){
		
		AppConfiguration.getInstance().setLang(lang);
		
	}

	/* Obtiene la hora actual como un long en un string */
	public String getNanoTime() {
		return ("" + System.nanoTime());
	}

	/*
	 * Actualiza las rutas a recursos que estan en los descriptores de los
	 * componentes
	 */
	private void updateComponentsPaths(String newpath, String oldpath)
			throws Exception {
		
		AppConfiguration apconf = AppConfiguration.getInstance();

		String oldlocalpath = apconf.getLocal(oldpath);
		String oldglobalpath = apconf.getGlobal(oldpath);

		String newlocalpath = apconf.getLocal(newpath);
		String newglobalpath = apconf.getGlobal(newpath);

		File dirxmls = new File(apconf.getEnginesXMLsPath());

		File[] files = dirxmls.listFiles();

		for (int i = 0; i < files.length; i++) {
			File xml = new File(files[i], files[i].getName() + ".xml");
			if ((xml!=null)&&(xml.isFile())){

				SAXBuilder builder = new SAXBuilder();
	
				Document document = null;
	
				try {
					document = builder.build(xml.getAbsolutePath());
				} catch (JDOMException e1) {
					String msg = "Error al leer descriptor XML de "
						+ files[i].getName();
					if (!AppConfiguration.getInstance().getLang().equals("es")){
						msg = "Error reading XML descriptor of "+ files[i].getName();
					}
					log.error(msg+xml.getAbsolutePath());			
					throw new XMLErrorException(msg);
					
					
				} catch (IOException e1) {
					String msg = "Error al leer descriptor XML de "
						+ files[i].getName();
					if (!AppConfiguration.getInstance().getLang().equals("es")){
						msg = "Error reading XML descriptor of "+ files[i].getName();
					}
					log.error(msg+xml.getAbsolutePath());			
					throw new XMLErrorException(msg);
				}
	
				boolean updated = false;
				
				Element root = document.getRootElement();
				
				Element pr = root.getChild("primitive",NS);
				if (pr!=null){					
					if (pr.getText().equals("false")){						
						Element dels = root.getChild("delegateAnalysisEngineSpecifiers",NS);
						if (dels!=null){
							List dlls = dels.getChildren("delegateAnalysisEngine", NS);
							for(int j=0;j<dlls.size();j++){							
								Element del = (Element)dlls.get(j);
								if (del!=null){								
									Element imp = del.getChild("import",NS);
									
									if (imp!=null){
										
										List is = imp.getAttributes();
										for (int k=0;k<is.size();k++){
											Attribute att = (Attribute)is.get(k);
											if (att.getName().equals("location")){
												String resourcePath = att.getValue();
												if (resourcePath.contains(oldpath)){
													resourcePath = resourcePath.replace(oldpath,newpath);
													att.setValue(resourcePath);
													if (!updated)
														updated=true;
													
												}
											}	
										}									
									}
								}
							}
						}		
					}
				}
				
				
				
				Element rmc = root.getChild("resourceManagerConfiguration", NS);
				
				if (rmc!=null){
					Element ers = rmc.getChild("externalResources", NS);
				
					if (ers!=null){
						
						List er = ers.getChildren("externalResource", NS);
			
						
						for (int j = 0; j < er.size(); j++) {
							Element frs = ((Element) er.get(j)).getChild(
									"fileResourceSpecifier", NS);
							if (frs != null) {
								Element fu = frs.getChild("fileUrl", NS);
								String path = fu.getText();
			
								if (path.contains(oldlocalpath)) {
									fu.setText(path.replace(oldlocalpath, newlocalpath));
									updated = true;
									System.out.println("updated local " + path);
								} else if (path.contains(oldglobalpath)) {
									fu.setText(path.replace(oldglobalpath, newglobalpath));
									updated = true;
									System.out.println("updated global " + path);
								}
			
							}
			
						}
					}
				}
				if (updated) {
					XMLOutputter outputter = new XMLOutputter();
					outputter.setFormat(Format.getPrettyFormat());
					FileOutputStream fos;
					try {
						fos = new FileOutputStream(xml.getAbsolutePath());
						outputter.output(document, fos);
						fos.flush();
						fos.close();
					} catch (IOException e) {
						String msg = "Error al escribir archivo XML";
						if (!AppConfiguration.getInstance().getLang().equals("es")){
							msg = "Error writing XML file";
						}
						log.error(msg+xml.getAbsolutePath());			
						throw new XMLErrorException(msg);
						
					}
				}
					
				
			}
		}
		
	}

	public String importTextFromURL(String url)throws Exception{
							
		String texto="";	
					
		try{					
				
			URL miURL;
		
			miURL = new URL(url);
				
			Source source = new Source(miURL);		
		
			au.id.jericho.lib.html.Element bodyElement=source.findNextElement(0,HTMLElementName.BODY);
			Segment contentSegment=(bodyElement==null) ? source : bodyElement.getContent();
			texto = contentSegment.getTextExtractor().setIncludeAttributes(true).toString();
									
						
		} catch (MalformedURLException e) {
			String msg = "Error de Sintaxis en URL";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Sintax error in URL";
			}
			log.error(msg);						
			throw new ProcessingException(msg);
		} catch (Exception e) {
			String msg = "Error al realizar conexión";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Connection Error";
			}
			log.error(msg);						
			throw new ProcessingException(msg);			
		}		
								
		return texto;				 
		
	}
	
	

}
