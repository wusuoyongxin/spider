package com.wusyx.spider.teach.download;

import com.wusyx.spider.teach.entity.Page;

public interface Downloadable {
	/**
	 * 下载页面
	 * @param url
	 * @return
	 */
	Page download(String url);
	
}
