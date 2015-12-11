package com.wusyx.spider.teach.util;


/**
 * 获取配置信息
 * @author wujianbo
 * @Date 2015年12月4日
 */
public class PropertiesUtil {

    /**
     * 属性文件加载对象
     */
    private static PropertiesLoader loader = new PropertiesLoader("properties/spider.properties", "properties/xpath.properties");
    
    /**
     * 获取配置
     * @see ${fns:getConfig('adminPath')}
     */
    public static String getConfig(String key) {
        String value = loader.getProperty(key);
        return value != null ? value : StringUtils.EMPTY;
    }
}
