package com.wusyx.spider.teach.repository;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang.StringUtils;

/**
 * 实现一个优先级的队列
 * @author Administrator
 *
 */
public class QueueRepository implements Repository {
	private Queue<String> heighqueue = new ConcurrentLinkedQueue<String>();//存储的列表页面的url
	private Queue<String> lowqueue = new ConcurrentLinkedQueue<String>();//存储的是商品的url
	@Override
	public String poll() {
		String url = heighqueue.poll();
		if(StringUtils.isBlank(url)){
			url = lowqueue.poll();
		}
		return url;
	}

	@Override
	public void addHeigh(String nextUrl) {
		this.heighqueue.add(nextUrl);
	}

	@Override
	public void add(String nextUrl) {
		this.lowqueue.add(nextUrl);
	}
	
}
