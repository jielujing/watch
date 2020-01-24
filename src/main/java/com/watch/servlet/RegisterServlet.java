package com.watch.servlet;

import com.watch.server.Request;
import com.watch.server.Response;
import com.watch.server.Servlet;

public class RegisterServlet implements Servlet {
	public void service(Request request, Response response) {
		
		response.print("注册成功");
	}

}
