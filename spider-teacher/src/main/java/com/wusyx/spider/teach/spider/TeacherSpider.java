package com.wusyx.spider.teach.spider;


import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.wusyx.spider.teach.download.Downloadable;
import com.wusyx.spider.teach.download.HttpClientDownload;
import com.wusyx.spider.teach.entity.Page;
import com.wusyx.spider.teach.process.Processable;
import com.wusyx.spider.teach.process.ZhjswProcess;
import com.wusyx.spider.teach.repository.QueueRepository;
import com.wusyx.spider.teach.repository.RandomRedisRepository;
import com.wusyx.spider.teach.repository.Repository;
import com.wusyx.spider.teach.store.ConsoleStore;
import com.wusyx.spider.teach.store.HbaseStore;
import com.wusyx.spider.teach.store.Storeable;
import com.wusyx.spider.teach.util.Config;
import com.wusyx.spider.teach.util.SleepUtils;
/**
 * 中华讲师网爬虫
 * @author wujianbo
 * @Date 2015年12月7日
 */
public class TeacherSpider{
	
	private static Logger logger = LoggerFactory.getLogger(TeacherSpider.class);
	private Downloadable downloadable = new HttpClientDownload();
	private Processable processable;
	private Storeable storeable = new ConsoleStore();
	private Repository repository = new QueueRepository();
	ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(Config.nThread);
	
	public TeacherSpider() {
		try {
			RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
			String zookeeperConnectionString = "127.0.0.1:2181";
//			String zookeeperConnectionString = "xiao125:2181,xiao124:2181,xiao121:2181";
			CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
			client.start();
			//获取当前主机ip
			InetAddress localHost = InetAddress.getLocalHost();
			String hostAddress = localHost.getHostAddress();
			client
			.create()
			.creatingParentsIfNeeded()
			.withMode(CreateMode.EPHEMERAL)
			.withACL(Ids.OPEN_ACL_UNSAFE)
			.forPath("/spider/"+hostAddress);
		} catch (Exception e) {
		    logger.error("{}", "启动爬虫注册zookeeper临时监控节点失败！");
		}
	}
	/**
	 * 执行爬虫
	 * @param args
	 */
	public static void main(String[] args) {
		TeacherSpider teacherSpider = new TeacherSpider();
		//下载
		//spider.setDownloadable(new HttpClientDownload());
		//解析
		teacherSpider.setProcessable(new ZhjswProcess());
		//存储
//		teacherSpider.setStoreable(new HbaseStore());
		//url队列
//		teacherSpider.setRepository(new RandomRedisRepository());
		
		String url = "http://www.jiangshi.org/searchLect.aspx?order=1";
		teacherSpider.setSeedUrl(url);
		teacherSpider.start();
    }
	
	/**
	 * 添加种子url
	 * @param url
	 */
	public void setSeedUrl(String url){
		this.repository.add(url);
	}
	
	/**
	 * 启动
	 */
	public void start() {
		//在开启爬虫之前需要做一个配置检查
		check();
		logger.info("开始启动爬虫.....");
		while(true){
			final String url = repository.poll();
			if(StringUtils.isNotBlank(url)){
				newFixedThreadPool.execute(new Runnable() {
					public void run() {
						//在 TeacherSpider的内部类中调用当前实例 [外部类TeacherSpider]的 download() 方法。
						//下载页面
						Page page = TeacherSpider.this.download(url);
						//页面内容不为空
						if (!Strings.isNullOrEmpty(page.getContent())) {
						    //解析页面
						    TeacherSpider.this.process(page);
						    //添加url到队列
						    List<String> urllist = page.getUrlList();
						    for (String nextUrl : urllist) {
						        if (nextUrl.startsWith("http://www.jiangshi.org/searchLect.aspx")) {
						            TeacherSpider.this.repository.addHeigh(nextUrl);
						        } else {
						            TeacherSpider.this.repository.add(nextUrl);
						        }
						    }
						    //如果解析的是商品列表页面，是不需要存储的
						    if (page.getUrl().endsWith(".jiangshi.org/")) {
						        //存储页面数据
						        TeacherSpider.this.store(page);
						    }
						    SleepUtils.sleep(Config.millions_1);
					    }
					}
				});
			}else{
				logger.info("暂时没有url...");
				SleepUtils.sleep(Config.millions_2);
			}
		}
	}
	
	/**
	 * 检查配置
	 */
	private void check() {
		if(processable==null){
			String message = "没有设置解析类";
			logger.error(message);
			throw new RuntimeException(message);
		}
		logger.info("==================爬虫使用的所有配置如下==========================");
		logger.info("downloadable的实现类是：{}",downloadable.getClass().getName());
		logger.info("processable的实现类是：{}",processable.getClass().getName());
		logger.info("storeable的实现类是：{}",storeable.getClass().getName());
		logger.info("repository的实现类是：{}",repository.getClass().getName());
		logger.info("=================================================================");
	}
	
	/**
	 * 下载页面
	 * @param url
	 * @return
	 */
	public Page download(String url) {
		return this.downloadable.download(url);
	}

	/**
	 * 解析页面
	 * @param page
	 */
	public void process(Page page) {
	    this.processable.process(page);
	}

	/**
	 * 存储数据
	 */
	public void store(Page page) {
		this.storeable.store(page);
	}

	public Downloadable getDownloadable() {
		return downloadable;
	}
	public void setDownloadable(Downloadable downloadable) {
		this.downloadable = downloadable;
	}
	public Processable getProcessable() {
		return processable;
	}
	public void setProcessable(Processable processable) {
		this.processable = processable;
	}
	public Storeable getStoreable() {
		return storeable;
	}
	public void setStoreable(Storeable storeable) {
		this.storeable = storeable;
	}
	public Repository getRepository() {
		return repository;
	}
	public void setRepository(Repository repository) {
		this.repository = repository;
	}

}
