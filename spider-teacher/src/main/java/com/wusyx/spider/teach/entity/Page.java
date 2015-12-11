package com.wusyx.spider.teach.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Page {
	/**
	 * 网页内容
	 */
	private String content;
	/**
	 * 页面的url
	 */
	private String url;
	/**
     * 行键(唯一id)
     */
    private String rowKey;
    /**
     * 基本信息
     */
    private Map<String,String> values = new HashMap<String, String>();
    /**
     * 存储详细信息的url的下一页 url列表
     */
    private List<String> urlList = new ArrayList<String>();
    
    /**
     * 添加基本信息
     * @param key
     * @param value
     */
    public void addField(String key,String value){
        this.values.put(key, value);
    }
    
    /**
     * 添加url
     * @param url
     */
    public void addUrl(String url){
        this.urlList.add(url);
    }
    
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getRowKey() {
        return rowKey;
    }
    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }
    public Map<String, String> getValues() {
        return values;
    }
    public List<String> getUrlList() {
        return urlList;
    }
	

}
