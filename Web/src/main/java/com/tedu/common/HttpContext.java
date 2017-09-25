package com.tedu.common;
/**
 * 定义HTTP协议中相关信息
 * @author adminitartor
 *
 */
public class HttpContext {
	/**
	 *  回车 
	 */
	public static final int CR = 13;
	/**
	 *  换行
	 */
	public static final int LF = 10;
	
	/**
	 *  状态码:成功
	 */
	public static final int STATUS_CODE_OK = 200;
	
	/**
	 *  状态描述:成功
	 */
	public static final String STATUS_VALUE_OK = "OK";
	
	
	/**
	 *  状态代码:未找到
	 */
	public static final int STATUS_CODE_NOT_FOUND = 404;
	
	/**
	 *  状态描述:未找到
	 */
	public static final String STATUS_VALUE_NOT_FOUND = "Not Found";
	
	/**
	 *  状态代码:错误
	 */
	public static final int STATUS_CODE_ERROR = 500;
	
	/**
	 *  状态描述:错误
	 */
	public static final String STATUS_VALUE_ERROR = "Internal Server Error";
	
}





