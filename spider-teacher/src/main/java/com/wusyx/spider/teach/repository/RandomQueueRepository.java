package com.wusyx.spider.teach.repository;

import java.util.HashMap;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.wusyx.spider.teach.util.DomainUtils;


/**
 * 随机获取某一个电商网站的数据
 * @author Administrator
 *
 */
public class RandomQueueRepository implements Repository {
	HashMap<String, Queue<String>> hashMap = new HashMap<String, Queue<String>>();
	Random random = new Random();
	@Override
	public String poll() {
		String[] keyarray = hashMap.keySet().toArray(new String[0]);
		int nextInt = random.nextInt(keyarray.length);
		String key = keyarray[nextInt];
		Queue<String> queue = hashMap.get(key);
		return queue.poll();
	}

	@Override
	public void addHeigh(String nextUrl) {
		String topDomain = DomainUtils.getTopDomain(nextUrl);
		Queue<String> queue = hashMap.get(topDomain);
		if(queue==null){
			queue = new ConcurrentLinkedDeque<String>();
			hashMap.put(topDomain, queue);
		}
		queue.add(nextUrl);
	}

	@Override
	public void add(String nextUrl) {
		addHeigh(nextUrl);
	}
	
	

}
