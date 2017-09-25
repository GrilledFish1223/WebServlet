package com.tedu.http;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.tedu.common.HttpContext;
import com.tedu.common.ServerContext;

/**
 * 封装Http响应
 * 
 * HTTP响应格式
 * 1:状态行
 * 2:响应头
 * 3:响应正文
 * 
 * 状态行
 * 状态行由3部分组成:协议版本，数字形式的状态代码，状态描述
 * 格式:Http_Version Status-code  Reason-Phrase CRLF
 * 例如:HTTP/1.1	 200 OK CRLF
 * 
 * 状态代码第一个数字有5种:
 * 1xx:指示信息，表示请求已接收，继续处理。
 * 2xx:成功，表示请求已接收，理解，接受
 * 3xx:重定向，要完成请求需要更进一步的操作
 * 4xx:客户端错误，请求语法错误或请求无法实现
 * 5xx:服务端错误，服务器未能实现该求情
 * 
 * 常见的状态码及描述:
 * 200 OK              客户端请求成功
 * 400 Bad Request     客户端请求有语法错误，服务端不能理解
 * 401 Unauthonzed     请求未授权
 * 403 Forbidden       服务端接收到请求，但是拒绝提供服务
 * 404 Not Found       请求的资源不存在
 * 500 Internal Server Error 服务器发生了不可预期的错误
 * 503 Service Unavailable   服务器当前不能够处理客户端请求
 * 
 * 响应头
 * 响应头注明很多返回的信息，按行输出
 * 常见：
 * Content-Type:用来指明发送给接收者的媒体类型
 * 常见的Content-Type:
 * text/html:    HTML格式文件(网页)
 * text/xml:     XML格式文件
 * image/gif:    gif格式图片
 * image/jpeg:   jpeg格式图片
 * image/png:    png格式图片
 * 
 * Content-Length:用来知名发送给接收者实体正文的长度
 *                简单说就是发送过去的数据的字节量
 * 
 * @author adminitartor
 *
 */
public class HttpResponse {
	private OutputStream out;
	/*
	 * 响应格式中需要的信息
	 */
	private int status; //状态行中的状态代码
	private String contentType; //响应头中的正文类型
	private int contentLength;//响应头中的正文长度
	
	//表示响应状态行以及响应头信息是否已经发送过
	private boolean hasPrintHeader;
	
	/*
	 * 记录所有可用状态码及描述
	 */
	private Map<Integer,String> statusMap;
	/**
	 * 构造方法
	 * @param out 对应客户端的输出流，通过该流将消息
	 *            响应给客户端
	 */
	public HttpResponse(OutputStream out){
		this.out = out;
		statusMap = new HashMap<Integer,String>();
		statusMap.put(HttpContext.STATUS_CODE_OK, HttpContext.STATUS_VALUE_OK);
		statusMap.put(HttpContext.STATUS_CODE_ERROR, HttpContext.STATUS_VALUE_ERROR);
		statusMap.put(HttpContext.STATUS_CODE_NOT_FOUND, HttpContext.STATUS_VALUE_NOT_FOUND);
	}
	public OutputStream getOutputStream() throws IOException {
		if(!hasPrintHeader){
			/*
			 * 获取输出流前，将状态行，响应头信息
			 * 自动发送
			 */
			//发送状态行
			println(ServerContext.protocol+" "+status+" "+statusMap.get(status));
			//发送响应头
			println("Content-Type:"+contentType);
			println("Content-Length:"+contentLength);
			println("");//单独发送空行，表示响应头发送完毕
			
			hasPrintHeader = true;
		}
		return out;
	}

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public int getContentLength() {
		return contentLength;
	}
	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}
	public Map<Integer, String> getStatusMap() {
		return statusMap;
	}
	
	/**
	 * 将给定的一行字符串以HTTP协议格式要求按行写出
	 * @param str
	 * @param out
	 * @throws IOException 
	 */
	private void println(String str)throws IOException{
		try {
			out.write(str.getBytes("ISO8859-1"));
			out.write(HttpContext.CR);
			out.write(HttpContext.LF);
		} catch (UnsupportedEncodingException e) {
			System.out.println("不受支持的编码");
			throw e;
		} catch (IOException e) {
			throw e;
		}
	}
	
	
}







