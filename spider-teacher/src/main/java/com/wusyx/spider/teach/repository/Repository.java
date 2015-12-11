package com.wusyx.spider.teach.repository;

/**
 * URL 队列仓库
 * @author wujianbo
 * @Date 2015年12月7日
 */
public interface Repository {

    /**
     * 获取url
     * @return
     */
	String poll();

	/**
	 * 添加高优先级url
	 * @param nextUrl
	 */
	void addHeigh(String nextUrl);

	/**
	 * 添加低优先级url
	 * @param nextUrl
	 */
	void add(String nextUrl);
	
}
