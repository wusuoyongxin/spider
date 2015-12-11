package com.wusyx.spider.teach.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wusyx.spider.teach.entity.Page;


public class ConsoleStore implements Storeable {
    
    private static Logger logger = LoggerFactory.getLogger(ConsoleStore.class);

	@Override
	public void store(Page page) {
	    logger.info("url:{}, rowKey:{}, name:{}, price:{}",page.getUrl(),page.getRowKey(),page.getValues().get("name"),page.getValues().get("price"));
	    logger.error("url:{}, rowKey:{}, name:{}, price:{}",page.getUrl(),page.getRowKey(),page.getValues().get("name"),page.getValues().get("price"));
	}

}
