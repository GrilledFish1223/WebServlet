package com.tedu.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.tedu.common.HttpContext;

/**
 * 封装Http请求相关内容
 * @author adminitartor
 *
 */
public class HttpRequest {
	//请求方法
	private String method;
	//请求资源
	private String uri;
	//请求协议
	private String protocol;
	
	//所有参数
	private Map<String,String> parameters;
	
	/**
	 * 构造方法，用于创建HttpRequset实例
	 * @param in 对应客户端的输入流，通过该流读取
	 *           客户端发送过来的请求信息并封装到
	 *           当前HttpRequest对象中
	 */
	public HttpRequest(InputStream in){
		/*
		 * HTTP协议中的请求信息格式:
		 * 请求行
		 * GET /index.html HTTP/1.1CRLF
		 * 
		 * 消息报头
		 * 消息正文
		 * 以行为单位发送至服务端 每行结尾以(CR LF)
		 * CR:回车   LF:换行
		 */
		try {
			/*
			 * 读取第一行字符串，请求行信息
			 * 格式:
			 * Method Request-URI Http-Version(CRLF)
			 * 如:
			 * GET /index.html HTTP/1.1CRLF
			 */
			String line = readLine(in);
			//判断读取行内容，防止解析客户端发送的空行
			if(line.length()>0){
				String[] data = line.split("\\s");
				method = data[0];
				uri = data[1];
				parseParametersByUri(uri);
				System.out.println(this.parameters);
				protocol = data[2];
			}
			
		} catch (IOException e) {
			
		}
		
	}
	/**
	 * 根据参数名获取参数值
	 * @param name
	 * @return
	 */
	public String getParameter(String name){
		if(this.parameters==null){
			return null;
		}else{
			return this.parameters.get(name);
		}
		
	}
	
	
	/**
	 * 根据URI解析用户传递的所有参数
	 * @param uri
	 */
	private void parseParametersByUri(String uri){
		if(uri.indexOf("?")>=000){
			this.parameters = new HashMap<String,String>();
			//截取所有参数
			String line = uri.substring(uri.indexOf("?")+1);
			//截取每一组参数
			String[] params = line.split("&");
			for(String param : params){
				String[] data = param.split("=");
				parameters.put(data[0], data[1]);
			}
		}
	}
	
	/**
	 * 从给定的输入流中读取一行字符串并将其返回。
	 * 当读取到CR LF时认为一行结束
	 *  
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private String readLine(InputStream in)
							throws IOException{
		/*
		 * 顺序的从流中读取每个字节并转换为对应的字符
		 * 然后拼接在一起，直到连续读取了CR LF时停止
		 * 并将拼接的字符串返回。
		 * 
		 * http://localhost:8080
		 * GET /index.html HTTP/1.1CRLF
		 */
		StringBuilder builder = new StringBuilder();
		try {
			int ch1 = -1 ,ch2 = -1;
			while((ch2 = in.read())!=-1){
				if(ch1==HttpContext.CR&&ch2==HttpContext.LF){
					break;
				}
				builder.append((char)ch2);
				ch1 = ch2;
			}
			return builder.toString().trim();
		} catch (IOException e) {
			throw e;
		}
	}


	public String getMethod() {
		return method;
	}


	public String getUri() {
		return uri;
	}


	public String getProtocol() {
		return protocol;
	}
	
	
	
}







