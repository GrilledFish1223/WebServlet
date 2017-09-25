package com.tedu.core;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tedu.common.ServerContext;

/**
 * 基于Http协议的Web服务端程序
 * @author adminitartor
 *
 */
public class WebServer {                	
	private ServerSocket server;
	
	/*
	 * 线程池，管理用于处理客户端请求的线程
	 */
	private ExecutorService threadPool;
	/**
	 * 构造方法，用于初始化服务端程序
	 * @throws Exception
	 */
	public WebServer() throws Exception{
		try {
			server = new ServerSocket(ServerContext.ServerPort);
			
			threadPool = Executors.newFixedThreadPool(
					ServerContext.max_thread);
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void start(){
		try {
			while(true){
				System.out.println("等待客户端连接...");
				Socket socket = server.accept();
				/*
				 * 将处理该客户端求情的任务交给线程池
				 */
				threadPool.execute(new ClientHandler(socket));
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void main(String[] args) {
		try {
			WebServer server = new WebServer();
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("服务端启动失败!");
		}
	}
}













