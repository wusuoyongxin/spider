package com.wusyx.spider.teach;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.wusyx.spider.teach.download.HttpClientDownload;
import com.wusyx.spider.teach.entity.Page;
import com.wusyx.spider.teach.spider.Spider;
import com.wusyx.spider.teach.spider.TeacherSpider;
import com.wusyx.spider.teach.util.PropertiesLoader;

public class SpiderTest {
	
	@Test
	public void testSpider() {
		Spider spider = new Spider();
		//spider.start();
		spider.setDownloadable(new HttpClientDownload());
		String url = "http://item.jd.com/1861097.html";
		Page page = spider.download(url);
		spider.process(page);
		spider.store(page);
	}
	
	@Test
	public void testTeacherSpider() {
		TeacherSpider spider = new TeacherSpider();
		//spider.start();
		spider.setDownloadable(new HttpClientDownload());
		List<String> urlList = new ArrayList<String>();
		urlList.add("http://songlianke.jiangshi.org/");
//		urlList.add("http://sunjunzheng.jiangshi.org/");
		for (String url : urlList) {
			System.err.println("url:" + url);
			Page page = spider.download(url);
			spider.process(page);
			spider.store(page);
			System.err.println("-----------------------------------------------------------------");
		}
	}
	
	@Test
	public void testPropertiesLoader(){
	    PropertiesLoader propertiesLoader = new PropertiesLoader("properties/spider.properties", "properties/xpath.properties");
	    String string = propertiesLoader.getProperty("aaa");
	    String string1 = propertiesLoader.getProperty("ccc");
	    System.out.println(string);
	    System.out.println(string1);
	}

	@Test
	public void testList(){
		ArrayList<Integer> list = new ArrayList<Integer>();
		Vector<Integer> vector = new Vector<Integer>();
		long start = System.currentTimeMillis();
		for(int i=0;i<1000000;i++)
			list.add(i);
		long end = System.currentTimeMillis();
		System.out.println("ArrayList进行1000000次插入操作耗时："+(end-start)+"ms");
		start = System.currentTimeMillis();
		for(int i=0;i<1000000;i++)
			vector.add(i);
		end = System.currentTimeMillis();
		System.out.println("Vector进行1000000次插入操作耗时："+(end-start)+"ms");
	}

	@Test
	public void testListException(){
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(2);
		Iterator<Integer> iterator = list.iterator();
		while(iterator.hasNext()){
			Integer integer = iterator.next();
			if(integer==2)
				list.remove(integer);
		}
	}


}
