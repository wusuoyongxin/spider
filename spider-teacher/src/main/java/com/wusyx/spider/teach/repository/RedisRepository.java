package com.wusyx.spider.teach.repository;

import org.apache.commons.lang.StringUtils;

import com.wusyx.spider.teach.util.RedisUtils;


/**
 * 这个优先级队列可以持久化
 * @author Administrator
 *
 */
public class RedisRepository implements Repository {
	RedisUtils redisUtils = new RedisUtils();

	@Override
	public String poll() {
		String url = redisUtils.poll(RedisUtils.heightkey);
		if(StringUtils.isBlank(url)){
			url = redisUtils.poll(RedisUtils.lowkey);
		}
		return url;
	}

	@Override
	public void addHeigh(String nextUrl) {
		redisUtils.add(RedisUtils.heightkey, nextUrl);
	}

	@Override
	public void add(String nextUrl) {
		redisUtils.add(RedisUtils.lowkey, nextUrl);
	}

}
