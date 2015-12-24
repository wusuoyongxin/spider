package com.wusyx.spider.teach.manage;

import java.util.List;

import com.wusyx.spider.teach.util.RedisUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class UrlJob implements Job {

	RedisUtils redisUtils = new RedisUtils();
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		List<String> lrange = redisUtils.lrange(RedisUtils.start_url, 0, -1);
		for (String url : lrange) {
			redisUtils.add(RedisUtils.heightkey, url);
		}
	}

}
