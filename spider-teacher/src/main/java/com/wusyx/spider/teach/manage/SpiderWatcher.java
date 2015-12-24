package com.wusyx.spider.teach.manage;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
/**
 * 
 * 这个监控进程只需要运行一个即可
 * 
 * 这个进程相当于是一个守护进程
 * @author Administrator
 *
 */
public class SpiderWatcher implements Watcher {
	CuratorFramework client;
	List<String> childrenList = new ArrayList<String>();
	public SpiderWatcher() {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		String zookeeperConnectionString = "192.168.1.170:2181";
		client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
		client.start();
		
		try {
			//注册监视器，这个监视器是需要重复注册的，只能使用一次。
			childrenList = client.getChildren().usingWatcher(this).forPath("/spider");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 当监控的节点下面的子节点发生变化的时候，这个方法会被调用
	 */
	@Override
	public void process(WatchedEvent event) {
		try {
			List<String> newChildrenList = client.getChildren().usingWatcher(this).forPath("/spider");
			for (String node : newChildrenList) {
				if(!childrenList.contains(node)){
					System.out.println("新增节点："+node);
				}
			}
			for (String node : childrenList) {
				if(!newChildrenList.contains(node)){
					System.out.println("节点消失："+node);
					//TODO-- 在这需要给管理员发送短信或者邮件提醒，
					/**
					 * 发邮件使用javamail
					 * 发短信使用第三方的工具，云片网
					 */
				}
			}
			//这行赋值的代码不能忘
			this.childrenList = newChildrenList;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		SpiderWatcher spiderWatcher = new SpiderWatcher();
		spiderWatcher.run();
	}

	private void run() {
		while(true){
			;
		}
	}

}
