package com.watch;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * 通过url或取配置文件对应的Servlet
 * @author lenovo-pc
 *
 */
public class WebApp {
	private static WebContext context;
	static {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();

			SAXParser parse = factory.newSAXParser();

			WebHandler handler = new WebHandler();

			parse.parse(Thread.currentThread().getContextClassLoader().getResourceAsStream("/conf/web.xml"), handler);
			context = new WebContext(handler.getEntitys(), handler.getMappings());
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public static Servlet getServletFromUrl(String url) {
		
		String className = context.getClz("/"+url);
		Class clz;
		Servlet servlet;
		try {
			clz = Class.forName(className);
			servlet = (Servlet) clz.getConstructor().newInstance();
			return servlet;			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
