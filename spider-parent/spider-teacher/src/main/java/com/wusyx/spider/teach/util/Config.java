package com.wusyx.spider.teach.util;

/**
 * 这个抽取方式是不规范的
 * 正常情况下需要把这些数值存储到数据库或者配置文件中
 * 建议使用数据库，使用 redis数据库
 */
public class Config {
	/**
	 * 线程的数量
	 */
	public static  int nThread = 5;
	/**
	 * 线程休息时间:1秒
	 */
	public static  int millions_1 = 1000;
	/**
     * 线程休息时间:2秒
     */
	public static long millions_2 = 2000;
	/**
	 * 线程休息时间:3秒
	 */
	public static int millions_3 = 3000;
	
	
	

}
