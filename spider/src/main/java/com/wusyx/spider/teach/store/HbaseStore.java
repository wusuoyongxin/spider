package com.wusyx.spider.teach.store;

import java.io.IOException;
import java.util.Map;

import com.google.common.base.Strings;
import com.wusyx.spider.teach.entity.Page;
import com.wusyx.spider.teach.util.HbaseUtils;
import com.wusyx.spider.teach.util.RedisUtils;

/**
 * 为什么要设计两个列簇呢
 * 默认情况下查看讲师信息，只需要查询hbase中的一个列簇即可，可以提高效率
 * #建表语句
 * create 'spider','teacher_info','teacher_course'
 */
public class HbaseStore implements Storeable {

	HbaseUtils hbaseUtils = new HbaseUtils();
	RedisUtils redisUtils = new RedisUtils();
	
	/**
     * HBASE 表名称
     */
    public static final String TABLE_NAME = "spider";
    /**
     * 列簇1-讲师信息
     */
    public static final String COLUMNFAMILY_1 = "teacher_info";
    /**
     * 列簇1中的列
     */
    public static final String COLUMNFAMILY_1_DATA_URL = "data_url";
    public static final String COLUMNFAMILY_1_NAME = "name";
    public static final String COLUMNFAMILY_1_PHOTO_URL = "photoUrl";
    public static final String COLUMNFAMILY_1_WEIXIN_URL = "weixinUrl";
    public static final String COLUMNFAMILY_1_AGE = "age";
    public static final String COLUMNFAMILY_1_CITY = "city";
    public static final String COLUMNFAMILY_1_REALM = "realm";
    public static final String COLUMNFAMILY_1_PROFESSION = "profession";
    public static final String COLUMNFAMILY_1_PRICE = "price";
    public static final String COLUMNFAMILY_1_PHONE = "phone";
    public static final String COLUMNFAMILY_1_INTRO_URL = "IntroUrl";
    public static final String COLUMNFAMILY_1_COURSE_URL = "CourseUrl";
    public static final String COLUMNFAMILY_1_COURSE_WARE_URL = "CourseWareUrl";
//    public static final String COLUMNFAMILY_1_ = "";
    
    /**
     * 列簇2-讲师课程
     */
    public static final String COLUMNFAMILY_2 = "teacher_course";
    /**
     * 列簇2中的列
     */
    public static final String COLUMNFAMILY_2_COURSE = "course";
    public static final String COLUMNFAMILY_2_QUALIFICATIONS = "qualifications";
    public static final String COLUMNFAMILY_2_TEACHINGSTYLE = "teachingstyle";
    public static final String COLUMNFAMILY_2_SERVICE = "service";
//    public static final String COLUMNFAMILY_2_ = "";
	
	@Override
	public void store(Page page) {
		String rowKey = page.getRowKey();
		//把需要建立索引的商品ID存到redis中
		redisUtils.add("solr_index", rowKey);
		Map<String, String> values = page.getValues();
		try {
			hbaseUtils.put(TABLE_NAME, rowKey, COLUMNFAMILY_1, COLUMNFAMILY_1_DATA_URL, page.getUrl());
			hbaseUtils.put(TABLE_NAME, rowKey, COLUMNFAMILY_1, COLUMNFAMILY_1_NAME, Strings.isNullOrEmpty(values.get(COLUMNFAMILY_1_NAME)) ? "" : values.get(COLUMNFAMILY_1_NAME));
			hbaseUtils.put(TABLE_NAME, rowKey, COLUMNFAMILY_1, COLUMNFAMILY_1_PHOTO_URL, Strings.isNullOrEmpty(values.get(COLUMNFAMILY_1_PHOTO_URL)) ? "" : values.get(COLUMNFAMILY_1_PHOTO_URL));
			hbaseUtils.put(TABLE_NAME, rowKey, COLUMNFAMILY_1, COLUMNFAMILY_1_WEIXIN_URL, Strings.isNullOrEmpty(values.get(COLUMNFAMILY_1_WEIXIN_URL)) ? "" : values.get(COLUMNFAMILY_1_WEIXIN_URL));
			hbaseUtils.put(TABLE_NAME, rowKey, COLUMNFAMILY_1, COLUMNFAMILY_1_AGE, Strings.isNullOrEmpty(values.get(COLUMNFAMILY_1_AGE)) ? "" : values.get(COLUMNFAMILY_1_AGE));
			hbaseUtils.put(TABLE_NAME, rowKey, COLUMNFAMILY_1, COLUMNFAMILY_1_CITY, Strings.isNullOrEmpty(values.get(COLUMNFAMILY_1_CITY)) ? "" : values.get(COLUMNFAMILY_1_CITY));
			hbaseUtils.put(TABLE_NAME, rowKey, COLUMNFAMILY_1, COLUMNFAMILY_1_REALM, Strings.isNullOrEmpty(values.get(COLUMNFAMILY_1_REALM)) ? "" : values.get(COLUMNFAMILY_1_REALM));
			hbaseUtils.put(TABLE_NAME, rowKey, COLUMNFAMILY_1, COLUMNFAMILY_1_PROFESSION, Strings.isNullOrEmpty(values.get(COLUMNFAMILY_1_PROFESSION)) ? "" : values.get(COLUMNFAMILY_1_PROFESSION));
			hbaseUtils.put(TABLE_NAME, rowKey, COLUMNFAMILY_1, COLUMNFAMILY_1_PRICE, Strings.isNullOrEmpty(values.get(COLUMNFAMILY_1_PRICE)) ? "" : values.get(COLUMNFAMILY_1_PRICE));
			hbaseUtils.put(TABLE_NAME, rowKey, COLUMNFAMILY_1, COLUMNFAMILY_1_PHONE, Strings.isNullOrEmpty(values.get(COLUMNFAMILY_1_PHONE)) ? "" : values.get(COLUMNFAMILY_1_PHONE));
			hbaseUtils.put(TABLE_NAME, rowKey, COLUMNFAMILY_1, COLUMNFAMILY_1_INTRO_URL, Strings.isNullOrEmpty(values.get(COLUMNFAMILY_1_INTRO_URL)) ? "" : values.get(COLUMNFAMILY_1_INTRO_URL));
			hbaseUtils.put(TABLE_NAME, rowKey, COLUMNFAMILY_1, COLUMNFAMILY_1_COURSE_URL, Strings.isNullOrEmpty(values.get(COLUMNFAMILY_1_COURSE_URL)) ? "" : values.get(COLUMNFAMILY_1_COURSE_URL));
			hbaseUtils.put(TABLE_NAME, rowKey, COLUMNFAMILY_1, COLUMNFAMILY_1_COURSE_WARE_URL, Strings.isNullOrEmpty(values.get(COLUMNFAMILY_1_COURSE_WARE_URL)) ? "" : values.get(COLUMNFAMILY_1_COURSE_WARE_URL));
			
			hbaseUtils.put(TABLE_NAME, rowKey, COLUMNFAMILY_2, COLUMNFAMILY_2_COURSE, Strings.isNullOrEmpty(values.get(COLUMNFAMILY_2_COURSE)) ? "" : values.get(COLUMNFAMILY_2_COURSE));
			hbaseUtils.put(TABLE_NAME, rowKey, COLUMNFAMILY_2, COLUMNFAMILY_2_QUALIFICATIONS, Strings.isNullOrEmpty(values.get(COLUMNFAMILY_2_QUALIFICATIONS)) ? "" : values.get(COLUMNFAMILY_2_QUALIFICATIONS));
			hbaseUtils.put(TABLE_NAME, rowKey, COLUMNFAMILY_2, COLUMNFAMILY_2_TEACHINGSTYLE, Strings.isNullOrEmpty(values.get(COLUMNFAMILY_2_TEACHINGSTYLE)) ? "" : values.get(COLUMNFAMILY_2_TEACHINGSTYLE));
			hbaseUtils.put(TABLE_NAME, rowKey, COLUMNFAMILY_2, COLUMNFAMILY_2_SERVICE, Strings.isNullOrEmpty(values.get(COLUMNFAMILY_2_SERVICE)) ? "" : values.get(COLUMNFAMILY_2_SERVICE));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
