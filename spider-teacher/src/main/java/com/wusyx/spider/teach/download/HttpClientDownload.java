package com.wusyx.spider.teach.download;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wusyx.spider.teach.entity.Page;
import com.wusyx.spider.teach.util.PageUtil;


public class HttpClientDownload implements Downloadable{
    
    private static Logger logger = LoggerFactory.getLogger(HttpClientDownload.class);
    
	public Page download(String url) {
		Page page = new Page();
		page.setContent(PageUtil.getContent(url));
		page.setUrl(url);
//		logger.info("页面内容：{}", page.getContent());
		return page;
	}
	
	

}
