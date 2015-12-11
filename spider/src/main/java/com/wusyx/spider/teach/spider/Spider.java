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

import com.wusyx.spider.teach.download.Downloadable;
import com.wusyx.spider.teach.download.HttpClientDownload;
import com.wusyx.spider.teach.entity.Page;
import com.wusyx.spider.teach.process.JdProcess;
import com.wusyx.spider.teach.process.Processable;
import com.wusyx.spider.teach.repository.QueueRepository;
import com.wusyx.spider.teach.repository.Repository;
import com.wusyx.spider.teach.store.ConsoleStore;
import com.wusyx.spider.teach.store.Storeable;
import com.wusyx.spider.teach.util.Config;
import com.wusyx.spider.teach.util.SleepUtils;


public class Spider {
	private Downloadable downloadable = new HttpClientDownload();
	private Processable processable;
	private Storeable storeable = new ConsoleStore();
	private Repository repository = new QueueRepository();
	Logger logger = LoggerFactory.getLogger(getClass());
	ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(Config.nThread);
	
	public Spider() {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		String zookeeperConnectionString = "127.0.0.1:2181";
		CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
		client.start();
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			String hostAddress = localHost.getHostAddress();
			
			client.create().
			creatingParentsIfNeeded().
			withMode(CreateMode.EPHEMERAL).
			withACL(Ids.OPEN_ACL_UNSAFE).
			forPath("/spider/"+hostAddress);
		} catch (Exception e) {
		    
		}
	}
	
	/**
	 * 开启一个爬虫
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
						Page page = Spider.this.download(url);
						Spider.this.process(page);
						List<String> urllist = page.getUrlList();
						for (String nextUrl : urllist) {
							if (nextUrl.startsWith("http://list.jd.com/")) {
								Spider.this.repository.addHeigh(nextUrl);
							} else {
								Spider.this.repository.add(nextUrl);
							}
						}
						//如果解析的是商品列表页面，是不需要存储的
						if (page.getUrl().startsWith("http://item.jd.com/")) {
							Spider.this.store(page);
						}
						SleepUtils.sleep(Config.millions_1);
					}
				});
				
			}else{
				logger.info("暂时没有url了！");
				SleepUtils.sleep(Config.millions_3);
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
		logger.info("========================================================");
	}
	/**
	 * 下载
	 * @param url 
	 * @return 
	 */
	public Page download(String url) {
		return this.downloadable.download(url);
	}
	/**
	 * 解析
	 * @param page 
	 */
	public void process(Page page) {
		this.processable.process(page);
	}
	
	/**
	 * 存储
	 * @param page 
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
	//添加种子url
	public void setSeedUrl(String url){
		this.repository.add(url);
	}
	
	
	public static void main(String[] args) {
		Spider spider = new Spider();
		//下载
		//spider.setDownloadable(new HttpClientDownload());
		//解析
		spider.setProcessable(new JdProcess());
		//存储
//		spider.setStoreable(new HbaseStore());
		spider.setStoreable(new ConsoleStore());
		//url队列
		//spider.setRepository(new QueueRepository());
		
		String url = "http://list.jd.com/list.html?cat=9987,653,655";
		spider.setSeedUrl(url);
		spider.start();
	}
	
}
