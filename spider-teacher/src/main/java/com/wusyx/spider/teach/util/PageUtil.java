package com.wusyx.spider.teach.util;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageUtil {
    private static Logger logger = LoggerFactory.getLogger(PageUtil.class);
		
	static HttpClientBuilder builder = HttpClients.custom();
	static{
		//设置浏览器的信息
//		builder.setUserAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.132 Safari/537.36");
		builder.setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
	}
	static HttpHost proxy = new HttpHost("218.97.194.198", 80);
//	static CloseableHttpClient client = builder.setProxy(proxy ).build();
	static CloseableHttpClient client = builder.build();
	
	public static String getContent(String url){
		String content = null;
		HttpGet request = new HttpGet(url);
		try {
			CloseableHttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			content = EntityUtils.toString(entity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
		    logger.error("下载页面失败：{}", url);
		    logger.error(e.getMessage());
//			e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		logger.info("下载页面完成：{}", url);
		return content;
	}
}
