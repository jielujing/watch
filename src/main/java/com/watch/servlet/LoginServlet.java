package com.watch.servlet;

import com.watch.server.Request;
import com.watch.server.Response;
import com.watch.server.Servlet;

public class LoginServlet implements Servlet {
	public void service(Request request, Response response) {
		response.print("<html>");
		response.print("<head>");
		response.print("<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\">\r\n");
		response.print("<title>");
		response.print("服务器响应成功");
		response.print("</title>");
		response.print("</head>");
		response.print("<body>");
		response.print("终于回来了"+request.getParameter("cc"));
		response.print("</body>");
		response.print("<html>");
	}

}
