package com.wusyx.spider.teach.process;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.wusyx.spider.teach.entity.Page;
import com.wusyx.spider.teach.util.HtmlUtil;
import com.wusyx.spider.teach.util.PageUtil;
import com.wusyx.spider.teach.util.PropertiesUtil;

/**
 * 中华讲师网-页面解析
 * @author wujianbo
 * @Date 2015年12月7日
 */
public class ZhjswProcess implements Processable {
    
    private static Logger logger = LoggerFactory.getLogger(ZhjswProcess.class);

    public void process(Page page) {
        String content = page.getContent();
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        TagNode rootNode = htmlCleaner.clean(content);//表示获取到文档对象的根节点了
        
        if (page.getUrl().startsWith("http://www.jiangshi.org/searchLect.aspx")) {
            //列表页面
            processTeacherList(page, rootNode);
        }else{
            //解析详情页面
            processTeacherInfo(page, rootNode);
        }
    }
    
    /**
     * 解析列表页面
     * @param page
     * @param rootNode
     */
    private void processTeacherList(Page page, TagNode rootNode) {
        //列表页面
        try {
            //解析下一页的url
                                                                            //*[@id=\"form1\"]/div[3]/div/div/div[1]/div[3]/div[22]/ul/li[9]/a
            String nexturl = HtmlUtil.getAttributeByName(rootNode, "href", "//*[@id=\"form1\"]/div/div/div/div[1]/div[3]/div[22]/ul/li[9]/a");
            if(!Strings.isNullOrEmpty(nexturl)){//针对最后一页做判断
                page.addUrl(nexturl);
            }
            //解析列表页面中的详情页面url
            Object[] evaluateXPath = rootNode.evaluateXPath("//*[@id=\"form1\"]/div/div/div/div[1]/div[3]/div/div[1]/a");
            if(evaluateXPath.length>0){
                for (Object object : evaluateXPath) {
                    TagNode tagNode = (TagNode)object;
                    page.addUrl(tagNode.getAttributeByName("href"));
                }
            }
        } catch (XPatherException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 解析详情页面
     * @param page
     * @param rootNode
     */
    private void processTeacherInfo(Page page, TagNode rootNode) {
        HtmlCleaner htmlCleaner = new HtmlCleaner();
       /* try {*/  
        Pattern p = Pattern.compile("http://([a-zA-z]+).jiangshi.org/");
        Matcher matcher = p.matcher(page.getUrl());
		String name = "";
		if(matcher.find()){
			name = matcher.group(1);
			page.setRowKey("zhjsw_"+name);
//			System.out.println("zhjsw_"+name);
		}
            //获取老师-姓名
            page.addField("name", HtmlUtil.getText(rootNode, PropertiesUtil.getConfig("zhjsw.teacher.title")));
            //获取老师-照片
            page.addField("photoUrl", HtmlUtil.getAttributeByName(rootNode, "src", PropertiesUtil.getConfig("zhjsw.teacher.photourl")));
            //获取老师-微信图片
            page.addField("weixinUrl",HtmlUtil.getAttributeByName(rootNode, "src", PropertiesUtil.getConfig("zhjsw.teacher.weixinurl")));
            //获取老师-年龄
            page.addField("age", HtmlUtil.getText(rootNode, PropertiesUtil.getConfig("zhjsw.teacher.age")));
            //获取老师-所在地
            page.addField("city", HtmlUtil.getText(rootNode, PropertiesUtil.getConfig("zhjsw.teacher.city")));
            //获取老师-擅长领域
            page.addField("realm", HtmlUtil.getText(rootNode, PropertiesUtil.getConfig("zhjsw.teacher.realm")));
            //获取老师-擅长行业
            page.addField("profession", HtmlUtil.getText(rootNode, PropertiesUtil.getConfig("zhjsw.teacher.profession")));
            //获取老师-费用
            page.addField("price", HtmlUtil.getText(rootNode, PropertiesUtil.getConfig("zhjsw.teacher.price")));
            //获取老师-电话 QQ
            page.addField("phone", HtmlUtil.getText(rootNode, PropertiesUtil.getConfig("zhjsw.teacher.phone")).trim().replace(" ", "").replaceAll("\r|\n", "").replaceAll("&nbsp;", " "));
            //获取老师-简介页面Url - introurl
            String IntroUrl = HtmlUtil.getAttributeByName(rootNode, "href", PropertiesUtil.getConfig("zhjsw.teacher.introurl"));
            page.addField("IntroUrl", IntroUrl);
            //获取老师-课程页面Url - courseurl
            String CourseUrl = HtmlUtil.getAttributeByName(rootNode, "href", PropertiesUtil.getConfig("zhjsw.teacher.courseurl"));
            page.addField("CourseUrl", CourseUrl);
            //获取老师-课件页面Url - CourseWareUrl
            String CourseWareUrl = HtmlUtil.getAttributeByName(rootNode, "href", PropertiesUtil.getConfig("zhjsw.teacher.coursewareurl"));
            page.addField("CourseWareUrl", CourseWareUrl);
            /**↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓老师-简介页面 Intro↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓**/
            //获取老师-简介页面 
            if (!Strings.isNullOrEmpty(IntroUrl)) {
                String IntroContent = PageUtil.getContent(IntroUrl);
                TagNode IntroRootNode = htmlCleaner.clean(IntroContent);//表示获取到文档对象的根节点了
                //获取老师-主讲课程
                page.addField("course", HtmlUtil.getText(IntroRootNode, PropertiesUtil.getConfig("zhjsw.teacher.course")).trim().replace(" ", "").replaceAll("\r|\n", ""));
                //获取老师-资历背景
                page.addField("qualifications", HtmlUtil.getText(IntroRootNode, PropertiesUtil.getConfig("zhjsw.teacher.qualifications")).trim().replace(" ", "").replaceAll("\r|\n", ""));
                //获取老师-授课风格
                page.addField("teachingstyle", HtmlUtil.getText(IntroRootNode, PropertiesUtil.getConfig("zhjsw.teacher.teachingstyle")).trim().replace(" ", "").replaceAll("\r|\n", ""));
                //获取老师-服务客户
                page.addField("service", HtmlUtil.getText(IntroRootNode, PropertiesUtil.getConfig("zhjsw.teacher.service")).trim().replace(" ", "").replaceAll("\r|\n", ""));
            }
            /**↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑老师-简介页面 Intro↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑**/
            
            /**↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓老师-课程页面 Course↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓**/
            //获取老师-课程页面 
            /*if (!Strings.isNullOrEmpty(CourseUrl)) {
                String CourseContent = PageUtil.getContent(CourseUrl);
                TagNode CourseRootNode = htmlCleaner.clean(CourseContent);//表示获取到文档对象的根节点了
            
                //获取老师- 课程介绍
                Object[] CourseEvaluateXPath = CourseRootNode.evaluateXPath("//*[@id=\"maincenter\"]/div[1]/div/div[1]/ul");
                if(CourseEvaluateXPath.length>0){
                    int i = 1;
                    System.out.println("课程介绍：");
                    for (Object object : (Object[])CourseEvaluateXPath) {
                        System.out.println("【" + i + "】：");
                        //去掉空行
                        System.out.println(((TagNode)object).getText().toString().trim().replace(" ", "").replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replaceAll("^((\r\n)|\n)", ""));//获取标签的内容
                        System.out.println();
                        i++;
                    }
                }
            
                 Object[]
                //获取老师-
                IntroEvaluateXPath = IntroRootNode.evaluateXPath("");
                if(IntroEvaluateXPath.length>0){
                    TagNode object = (TagNode)IntroEvaluateXPath[0];
                    System.out.println(object.getText().toString());//获取标签的内容
                }
                
            }*/
            /**↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑老师-课程页面 Course↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑**/
            
            /**↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓老师-课件页面 CourseWare↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓**/
            //获取老师-课件页面 
            /*if (!Strings.isNullOrEmpty(CourseWareUrl)) {
                String CourseWareContent = PageUtil.getContent(CourseWareUrl);
                TagNode CourseWareRootNode = htmlCleaner.clean(CourseWareContent);//表示获取到文档对象的根节点了
            
                //获取老师- 课件
                Object[] CourseWareEvaluateXPath = CourseWareRootNode.evaluateXPath("//*[@id=\"main\"]/div[2]/div/div");
                if(CourseWareEvaluateXPath.length>0){
                    int i = 1;
                    System.out.println("课件：");
                    for (Object object : (Object[])CourseWareEvaluateXPath) {
                        System.out.println("【" + i + "】：");
                        //去掉空行
                        System.out.println(((TagNode)object).getText().toString().trim().replace(" ", "").replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replaceAll("^((\r\n)|\n)", ""));//获取标签的内容
                        System.out.println();
                        i++;
                    }
                }
            }*/    
            /**↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑老师-课件页面 CourseWare↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑**/   
        /*} catch (XPatherException e) {
            logger.error("页面：{}，解析失败。。。", page.getUrl());
        }*/
    }

}
