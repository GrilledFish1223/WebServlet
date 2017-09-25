package com.tedu.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import com.tedu.common.HttpContext;
import com.tedu.common.ServerContext;
import com.tedu.http.HttpRequest;
import com.tedu.http.HttpResponse;

/**
 * 处理客户端请求
 * @author adminitartor
 *
 */
public class ClientHandler implements Runnable{
	private Socket socket;
	
	public ClientHandler(Socket socket){
		this.socket = socket;
	}
	
	public void run(){
		try {					
			//获取输入流，读取客户端发送过来的数据
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			
			//生成HttpRequest表示客户端请求信息
			HttpRequest request = new HttpRequest(in);
			//生成HttpResponse表示客户端响应信息
			HttpResponse response = new HttpResponse(out);
			if(request.getUri()!=null){
				/*
				 * 如果是注册功能，交由注册方法处理
				 */
				if(request.getUri().startsWith("/reg?")){
					service(request, response);
				}else{				
					/*
					 * HTTP协议要求实际响应客户端时的数据格式:
					 * HTTP/1.1 200 OK CRLF         状态行
					 * Content-Type:text/html CRLF  响应头信息
					 * Content-Length:100CRLF       响应头信息
					 * CRLF        单独发送CRLF指明响应头全部发送完毕
					 * DATA        实际数据           
					 */
					//加载客户端需要访问的文件资源
					File file = new File(ServerContext.web_root+request.getUri());
					System.out.println("请求的资源名:"+file.getName());
					//访问的资源存在才响应该资源
					if(file.exists()){
						//设置状态行
						response.setStatus(HttpContext.STATUS_CODE_OK);
						//设置响应头
						response.setContentType(getContentTypeByFile(file));
						response.setContentLength((int)file.length());
						responseFile(file, response);
						System.out.println("响应客户端完毕!");
					}else{
						//响应客户端没有该资源(显示个错误页面)
						response.setStatus(HttpContext.STATUS_CODE_NOT_FOUND);
						//错误页面
						file = new File(ServerContext.web_root+File.separator+ServerContext.notFoundPage);
						response.setContentType(getContentTypeByFile(file));
						response.setContentLength((int)file.length());
						responseFile(file, response);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			/*
			 * 响应500
			 */
			
		} finally{
			try {
				socket.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public void service(HttpRequest request,HttpResponse response){
		try {
			System.out.println("准备注册！");
			//获取用户名
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			
			PrintWriter pw = new PrintWriter(
				new FileOutputStream("userinfo.txt",true)	
			);
			pw.println(username+","+password);
			pw.close();
			//响应注册成功页面
			response.setStatus(HttpContext.STATUS_CODE_OK);
			File file = new File(ServerContext.web_root+"/regsuccess.html");
			response.setContentType(getContentTypeByFile(file));
			response.setContentLength((int)file.length());
			responseFile(file, response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 根据给定的文件获取对应的Content-Type
	 * @param file
	 * @return
	 */
	private String getContentTypeByFile(File file){
		/*
		 * 1:首选获取该文件的名字
		 * 2:获取该文件名的文件类型(文件名的后缀)
		 * 3:根据类型名从ServerContext的types中
		 *   做为key获取对应的Content-Type
		 */
		String fileName = file.getName();
		String ext = fileName.substring(
			fileName.lastIndexOf(".")+1
		);
		String contentType = ServerContext.types.get(ext);
		System.out.println("contentType:"+contentType);
		return contentType;
	}
	
	/**
	 * 响应文件
	 * @param file
	 * @param res
	 * @throws IOException 
	 */
	private void responseFile(File file,HttpResponse res) throws IOException{
		BufferedInputStream bis = null;
		try{
			/*
			 * 将该文件中每个字节通过客户端输出流发送给客户端
			 */
			bis = new BufferedInputStream(
					new FileInputStream(file)	
				);
			/*
			 * 通过Response取出输出流，这里就自动发送
			 * 了状态行和响应头
			 */
			OutputStream out = res.getOutputStream();
			int d = -1;
			while((d = bis.read())!=-1){
				out.write(d);
			}
		}catch(IOException e){
			throw e;
		}finally{
			if(bis != null){
				bis.close();
			}
		}
	}
	
	
}







