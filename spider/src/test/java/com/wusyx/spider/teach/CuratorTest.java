package com.wusyx.spider.teach;

import static org.junit.Assert.*;

import java.net.InetAddress;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.junit.Test;

public class CuratorTest {
	
	@Test
	public void test() throws Exception {
		//获取curator客户端
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		String zookeeperConnectionString = "192.168.1.170:2181";
		CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
		client.start();
		client.create()//表示创建节点
		.creatingParentsIfNeeded()//如果父节点不存在，则创建父节点，创建的父节点是永久节点
		.withMode(CreateMode.EPHEMERAL)//指定创建节点的类型，EPHEMERAL表示是临时节点类型
		.withACL(Ids.OPEN_ACL_UNSAFE)//指定权限
		.forPath("/spider/192.168.1.170");//指定节点的名称
		while(true){
			;
		}
	}
	
	@Test
	public void test2() throws Exception {
		InetAddress localHost = InetAddress.getLocalHost();
		String hostAddress = localHost.getHostAddress();
		System.out.println(hostAddress);
	}

}
