package com.wusyx.spider.teach.manage;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

public class UrlManager {
	
	public static void main(String[] args) {
		try {
			//获取一个默认调度器
			Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();
			//开启调度器
			defaultScheduler.start();
			
			//这个是一个需要被调度的任务
			JobDetail jobDetail = new JobDetail("url-job", Scheduler.DEFAULT_GROUP, UrlJob.class);
			//指定说明时候执行任务
			CronTrigger trigger = new CronTrigger("url-job", Scheduler.DEFAULT_GROUP, "35 48 10 * * ?");
			//添加一个调度任务
			defaultScheduler.scheduleJob(jobDetail , trigger);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
