package com.watch.BIOServer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

public class Response {
	private BufferedWriter bw;
	// 正文
	private StringBuilder content;
	// 协议头信息:状态行、请求头、回车
	private StringBuilder headinfo;
	private int len = 0;// 正文字节数

	private final String blank = " ";
	private final String CRLF = "\r\n";

	private Response() {
		content = new StringBuilder();
		headinfo = new StringBuilder();

	}

	public Response(Socket client) {
		this();
		try {
			bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			headinfo=null;
		}
	}

	public Response(OutputStream os) {
		this();
		bw = new BufferedWriter(new OutputStreamWriter(os));
	}

	// 动态添加内容
	public Response print(String info) {
		content.append(info);
		len += info.getBytes().length;
		return this;
	}

	public Response println(String info) {
		content.append(info).append(CRLF);
		len += (info + CRLF).getBytes().length;
		return this;
	}

	// 构建头信息 code:状态码
	private void createHeadinfo(int code) {
		// 1、响应行: HTTP/1.1 code OK
		headinfo.append("HTTP/1.1").append(blank);
		headinfo.append(code).append(blank);
		switch (code) {
		case 200:
			headinfo.append("OK").append(CRLF);
			break;
		case 404:
			headinfo.append("Not Found").append(CRLF);
			break;
		case 505:
			headinfo.append("Server Error").append(CRLF);
			break;
		}
		// 2、响应头(最后一行存在空行):
		/*
		 * Date:Mon,31Dec209904:25:57GMT Server:shsxt Server/0.0.1;charset=GBK
		 * Content-type:text/html Content-length:39725426
		 */
		headinfo.append("Date:").append(new Date()).append(CRLF);
		headinfo.append("Server:").append("lhh Server/0.0.1;charset=GBK").append(CRLF);
		headinfo.append("Content-type:text/html").append(CRLF);
		headinfo.append("Content-length:").append(len).append(CRLF);
		headinfo.append(CRLF);
	}

	// 推送响应信息
	public void pushToBrowser(int code) throws IOException {
		if(null==headinfo) {
			code=505;
		}
		createHeadinfo(code);
		bw.append(headinfo);
		bw.append(content);
		bw.flush();

	}
}
