package com.watch.BIOServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Dispatcher implements Runnable {
	private Socket client;
	private Response response;
	private Request request;
	private Servlet servlet;

	public Dispatcher(Socket client) {
		this.client = client;
		try {
			//初始化request和response。将输入流和参数放入request，将输出流放入response
			request = new Request(client);
			response = new Response(client);
		} catch (IOException e) {
			e.printStackTrace();
			try {
				client.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void run() {
		try {
			servlet = WebApp.getServletFromUrl(request.getUrl());
			if (null != servlet) {
				servlet.service(request, response);
				// 关注了状态码
				response.pushToBrowser(200);
			} else {
				InputStream is =Thread.currentThread().getContextClassLoader().getResourceAsStream("page/error.html");
				byte[] datas=new byte[1024*1024];
				int len=is.read(datas);
				response.print(new String(datas,0,len));
				response.pushToBrowser(404);
			}
		} catch (IOException e) {
			e.printStackTrace();
			try {
				response.pushToBrowser(500);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
