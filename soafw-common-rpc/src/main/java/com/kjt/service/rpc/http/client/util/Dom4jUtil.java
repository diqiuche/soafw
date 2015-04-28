package com.kjt.service.rpc.http.client.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.kjt.service.common.log.Logger;
import com.kjt.service.common.log.LoggerFactory;

public class Dom4jUtil {
	private static Logger logger = LoggerFactory.getLogger(Dom4jUtil.class);
	public static void writerToXML(String str){
		File file = new File("a.xml");  
		if (file.exists()){
			file.delete();
		}
			
	    if(!file.exists()) {  
	        try {  
	            file.createNewFile();  
	        } catch (IOException e) {  
	            logger.error(e); 
	        }  
	    }  
	    BufferedWriter bw;  
	    try {  
	        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));  
	        bw.append(str);  
	       // bw.newLine();  
	        bw.flush();  
	        bw.close();  
	    } catch (IOException e) {  
	        logger.error(e); 
	    }  
		
	}
	
	public static void readXMLfromFile(){
		SAXReader  sr = new SAXReader();
	    try {
			Document doc = sr.read("a.xml");
			Element el_root = doc.getRootElement();
			Iterator iter = el_root.elementIterator();
			
			while(iter.hasNext()){
				Object obj = iter.next();
				Element el = (Element)obj;
				String str = el.getText();
				logger.info("str==="+str);
				logger.info(el.getName());
				Iterator iter_row = el.elementIterator();
				
				while(iter_row.hasNext()){
					Element el_name = (Element) iter_row.next();
					Iterator item_iter = el_name.elementIterator();
					//logger.info("el_name="+el_name.getName());
					 while(item_iter.hasNext()){
						 Element item = (Element) item_iter.next();
						  if(item.getName().equals("ShipTypeID")){
							  logger.info("ShipTypeID="+item.getText());
						  }
						  if(item.getName().equals("ShipTypeName")){
							  logger.info("ShipTypeName="+item.getText());
						  }
					 }
				}
				
			}
		} catch (DocumentException e) {
		    logger.error(e); 
		}
	}
	
	public static void readXmlFromFileBySelect(){
		
		SAXReader sr = new SAXReader();
		
		try {
			Document doc =  sr.read("a.xml");
			List<Object> list = doc.selectNodes("/root/Rows/item");
			
			for(Object obj:list){
				logger.info("obj====="+obj); 
				Element et = (Element) obj;
				  if(et.getName().equals("ShipTypeID")){
				      logger.info("ShipTypeID="+et.getText());
				  }
				  if(et.getName().equals("ShipTypeName")){
				      logger.info("ShipTypeName="+et.getText());
				  }
			}
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
		
	}
	

	public static void main(String[] args) {
		writerToXML(null);
		readXMLfromFile();
		//readXmlFromFileBySelect();
	}

}
