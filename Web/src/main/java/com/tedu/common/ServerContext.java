package com.tedu.common;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 记录服务端相关信息
 * @author adminitartor
 *
 */
public class ServerContext {
	/*
	 * 服务端口
	 */
	public static int ServerPort;
	/*
	 * 服务端最大并发数
	 */
	public static int max_thread;
	/*
	 * 服务器应用根目录
	 */
	public static String web_root;
	/*
	 * 服务器使用的Http协议的版本
	 */
	public static String protocol;
	/*
	 * 根据媒体文件后缀对应Content-Type的类型
	 */
	public static Map<String,String> types = new HashMap<String,String>();
	/*
	 * 404页面
	 */
	public static String notFoundPage;
	
	static{
		//初始化ServerContext的静态成员
		init();
	}
	/**
	 * 加载sevrer.xml文件对ServerContext进行初始化
	 */
	private static void init(){
		try {
			//1创建SAXReader
			SAXReader reader = new SAXReader();
			
			Document doc = reader.read(new FileInputStream(
				"config"+File.separator+"server.xml")
			);
			
			Element root = doc.getRootElement();
			/*
			 * 解析<service>下相关信息
			 */
			Element serviceEle = root.element("service");
			//解析<connector>
			Element connEle = serviceEle.element("connector");
			
			protocol = connEle.attributeValue("protocol");
			
			System.out.println("protocol:"+protocol);
			
			ServerPort = Integer.parseInt(
				connEle.attributeValue("port")	
			);
			
			System.out.println("serverPort:"+ServerPort);
			max_thread = Integer.parseInt(
				connEle.attributeValue("maxThread")
			);
			System.out.println("max_thread:"+max_thread);
			
			//解析<webroot>
			web_root = serviceEle.elementText("webroot");
			System.out.println("web_root:"+web_root);
			
			//解析<not-found-page>
			notFoundPage = serviceEle.elementText("not-found-page");
			System.out.println("notFoundPage:"+notFoundPage);
			/*
			 * 解析<type-mappings>
			 */
			Element mappingsEle = root.element("type-mappings");
			List<Element> mappingsList = mappingsEle.elements();
			for(Element mapping : mappingsList){
				types.put(mapping.attributeValue("ext"), 
						  mapping.attributeValue("type"));
			}
			System.out.println("types:"+types);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}






