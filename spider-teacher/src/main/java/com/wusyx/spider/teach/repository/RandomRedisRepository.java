package com.wusyx.spider.teach.repository;

import java.util.HashMap;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.wusyx.spider.teach.util.DomainUtils;
import com.wusyx.spider.teach.util.RedisUtils;


/**
 * 随机获取某一个电商网站的数据
 * @author Administrator
 *
 */
public class RandomRedisRepository implements Repository {
	//key:网站域名  value是redis中的list列表的名称
	HashMap<String, String> hashMap = new HashMap<String,String>();
	Random random = new Random();
	RedisUtils redisUtils = new RedisUtils();
	@Override
	public String poll() {
		String[] keyarray = hashMap.keySet().toArray(new String[0]);
		int nextInt = random.nextInt(keyarray.length);
		String key = keyarray[nextInt];
		String value = hashMap.get(key);
		return redisUtils.poll(value);
	}

	@Override
	public void addHeigh(String nextUrl) {
		String topDomain = DomainUtils.getTopDomain(nextUrl);
		String value = hashMap.get(topDomain);
		if(value==null){
			value = topDomain;
			hashMap.put(topDomain, value);//key表示网站的顶级域名，value表示redis中list列表的名称
		}
		redisUtils.add(topDomain, nextUrl);
	}

	@Override
	public void add(String nextUrl) {
		addHeigh(nextUrl);
	}
	
	

}
