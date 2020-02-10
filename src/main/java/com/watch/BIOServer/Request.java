package com.watch.BIOServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 封装请求协议：封装请求参数为map
 * 
 * @author lenovo-pc
 *
 */
public class Request {
	private String requestinfo;
	// 请求方式
	private String method;
	// 请求url
	private String url;
	// 请求参数
	private String queryStr;
	// 存储参数
	private Map<String, List<String>> parameterMap;
	private int len;
	private byte[] datas;
	private final String CRLF = "\r\n";

	public Request(InputStream is) {
		parameterMap = new HashMap<>();
		datas = new byte[1024 * 1024];
		try {
			this.len = is.read(datas);
			this.requestinfo = new String(datas, 0, len);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		parseRequestinfo();
	}

	public Request(Socket client) throws IOException {
		this(client.getInputStream());
	}

	// 分解字符串
	private void parseRequestinfo() {
		System.out.println(requestinfo);
		System.out.println("------------开始分解字符串--------------");
		System.out.println("-----1、获取请求方式：开头到第一个/----------");
		this.method = this.requestinfo.substring(0, this.requestinfo.indexOf("/")).trim();
		System.out.println(this.method);
		System.out.println("-----2、获取请求url：第一个/到HTTP/，可能包含请求参数--------");
		int startidx = this.requestinfo.indexOf("/") + 1;
		int endidx = this.requestinfo.indexOf("HTTP/");
		this.url = this.requestinfo.substring(startidx, endidx).trim();
		// 获取？的位置
		int queryidx = this.url.indexOf("?");
		if (queryidx >= 0) {// 表示存在请求参数
			String[] urlArray = this.url.split("\\?");
			this.url = urlArray[0];
			queryStr = urlArray[1].trim();
		}
		// 最终的url
		System.out.println(this.url);
		System.out.println("-----3、获取请求参数：如果是GET，上一段代码已经获取，如果POST，请求体中可能还有参数--------");
		if (method.equals("POST")) {
			String qstr = this.requestinfo.substring(this.requestinfo.lastIndexOf(CRLF)).trim();
			if (null == queryStr)
				queryStr = qstr;
			else
				queryStr += "&" + qstr;
		}
		queryStr = null == queryStr ? "" : queryStr;
		System.out.println(queryStr);
		// 转成map
		convertMap();
	}

	// 处理请求参数为Map
	private void convertMap() {
		// 1、分割字符串 &
		String[] keyValues = this.queryStr.split("&");
		for (String queryStr : keyValues) {
			// 2、再次分割字符串 =
			String[] kv = queryStr.split("=");
			kv = Arrays.copyOf(kv, 2);
			// 获取key和value
			String key = kv[0];
			String value = kv[1] == null ? null : decode(kv[1], "utf-8");
			// 存储到Map中
			if (!parameterMap.containsKey(key)) { // 第一次
				parameterMap.put(key, new ArrayList<String>());
			}
			parameterMap.get(key).add(value);
		}
	}
	
	/**
	 * 处理中文
	 * @return
	 */
	private String decode(String value,String enc) {
		try {
			return java.net.URLDecoder.decode(value, enc);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 通过name获取对应的多个值
	 * @param key
	 * @return
	 */
	public String[] getParameterValues(String key) {
		List<String> values = this.parameterMap.get(key);
		if(null==values || values.size()<1) {
			return null;
		}
		return values.toArray(new String[0]);
	}
	/**
	 * 通过name获取对应的一个值
	 * @param key
	 * @return
	 */
	public String getParameter(String key) {
		String []  values =getParameterValues(key);
		return values ==null?null:values[0];
	}
	public String getMethod() {
		return method;
	}

	public String getUrl() {
		return url;
	}
	
	public String getQueryStr() {
		return queryStr;
	}
	
}
