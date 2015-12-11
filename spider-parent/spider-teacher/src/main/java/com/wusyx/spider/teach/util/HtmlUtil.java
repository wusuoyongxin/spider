package com.wusyx.spider.teach.util;

import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 解析页面
 * @author wujianbo
 * @Date 2015年12月4日
 */
public class HtmlUtil {
	private static Logger logger = LoggerFactory.getLogger(HtmlUtil.class);
    /**
     * 获取标签的值
     * @param tagNode
     * @param xpath
     * @return
     */
    public static String getText(TagNode tagNode,String xpath){
        String result=null;
        Object[] evaluateXPath;
        try {
            evaluateXPath = tagNode.evaluateXPath(xpath);
            if(evaluateXPath.length>0){
                TagNode node = (TagNode)evaluateXPath[0];
                result = node.getText().toString();
            }
        } catch (XPatherException e) {
            e.printStackTrace();
        }
//        logger.debug(result);
        return result;
    }
    /**
     * 获取标签的中的某个属性的值
     * @param tagNode
     * @param att
     * @param xpath
     * @return
     */
    public static String getAttributeByName(TagNode tagNode,String att,String xpath){
        String result=null;
        Object[] evaluateXPath;
        try {
            evaluateXPath = tagNode.evaluateXPath(xpath);
            if(evaluateXPath.length>0){
                TagNode node = (TagNode)evaluateXPath[0];
                result = node.getAttributeByName(att);
            }
        } catch (XPatherException e) {
            e.printStackTrace();
        }
        return result;
    }
}
