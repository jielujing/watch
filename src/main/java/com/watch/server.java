package com.watch;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 多线程处理，加入分发器
 *
 * @author lenovo-pc
 *
 */
public class server {
	ServerSocket serverSocket;
	private boolean isRunning;

	public static void main(String[] args) {
		server server = new server();
		server.start();
	}

	public void start() {
		try {
			serverSocket = new ServerSocket(8888);
			isRunning = true;
			receive();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("启动失败");
			stop();
		}
	}

	public void receive() {
		while (isRunning) {
			try {
				Socket client = serverSocket.accept();
				System.out.println("一个客户端建立了连接");
				new Thread(new Dispatcher(client)).start();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("客户端错误");
			}
		}
	}

	public void stop() {
		isRunning = false;
		try {
			this.serverSocket.close();
			System.out.println("服务器已停止");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
