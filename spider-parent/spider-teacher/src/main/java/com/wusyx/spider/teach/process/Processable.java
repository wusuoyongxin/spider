package com.wusyx.spider.teach.process;

import com.wusyx.spider.teach.entity.Page;

/**
 * 页面解析
 * @author wujianbo
 * @Date 2015年12月7日
 */
public interface Processable {
    
    /**
     * 解析页面
     * @param page
     */
	void process(Page page);

}
