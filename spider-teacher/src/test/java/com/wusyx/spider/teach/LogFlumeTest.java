package com.wusyx.spider.teach;

import java.util.Date;

import org.apache.http.client.utils.DateUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogFlumeTest {

	@Test
	public void appendTest() throws InterruptedException {
		Logger logger = LoggerFactory.getLogger(getClass());
		while (true) {
			logger.info("【中文】time:{},{}", System.currentTimeMillis(), DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			Thread.currentThread().sleep(1000);
		}
	}
}
