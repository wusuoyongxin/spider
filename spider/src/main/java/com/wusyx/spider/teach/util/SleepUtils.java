package com.wusyx.spider.teach.util;

public class SleepUtils {
    
	/**
	 * 线程休眠（毫秒）
	 * @param millions
	 */
	@SuppressWarnings("static-access")
    public static void sleep(long millions){
		try {
			Thread.currentThread().sleep(millions);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
