package com.wusyx.spider.teach;

import static org.junit.Assert.*;

import org.junit.Test;

import com.wusyx.spider.teach.util.PageUtil;


public class YiXunTest {
	@Test
	public void testName() throws Exception {
		String content = PageUtil.getContent("http://item.yixun.com/item-2178602.html?YTAG=3.705856212000");
		System.out.println(content);
	}

}
