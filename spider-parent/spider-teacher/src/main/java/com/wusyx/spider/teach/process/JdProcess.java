package com.wusyx.spider.teach.process;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.util.HtmlUtils;

import com.wusyx.spider.teach.entity.Page;
import com.wusyx.spider.teach.util.HtmlUtil;
import com.wusyx.spider.teach.util.PageUtil;


public class JdProcess implements Processable {
	/**
	 * 注意：商品的列表页面和明细页面解析规则不同
	 */
	@Override
	public void process(Page page) {
		String content = page.getContent();
		HtmlCleaner htmlCleaner = new HtmlCleaner();
		TagNode rootNode = htmlCleaner.clean(content);//表示获取到文档对象的根节点了
		if(page.getUrl().startsWith("http://item.jd.com/")){//表示是商品明细页面
			parseProcutor(page, rootNode);
		}else{
			try {
				//解析下一页的url
				String nexturl = HtmlUtil.getAttributeByName(rootNode, "href", "//*[@id=\"J_topPage\"]/a[2]");
				if(!nexturl.equals("javascript:;")){//针对最后一页做判断
					page.addUrl("http://list.jd.com"+nexturl.replace("&amp;", "&"));
				}
				//解析商品列表页面中的商品url
				Object[] evaluateXPath = rootNode.evaluateXPath("//*[@id=\"plist\"]/ul/li/div/div[1]/a");
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
	}

	/**
	 * 解析商品详细信息
	 * @param page
	 * @param rootNode
	 */
	public void parseProcutor(Page page, TagNode rootNode) {
		try {
			//获取商品标题
			page.addField("title", HtmlUtil.getText(rootNode, "//*[@id=\"name\"]/h1"));
			
			//获取商品的图片
			page.addField("picurl", "http:"+HtmlUtil.getAttributeByName(rootNode, "src", "//*[@id=\"spec-n1\"]/img"));
			
			//解析价格
			String url = page.getUrl();
			Pattern p = Pattern.compile("http://item.jd.com/([0-9]+).html");
			Matcher matcher = p.matcher(url);
			String goodsId = "";
			if(matcher.find()){
				goodsId = matcher.group(1);
				page.setRowKey("jd_"+goodsId);
			}
			String price_content = PageUtil.getContent("http://p.3.cn/prices/get?skuid=J_"+goodsId);
			JSONArray jsonArray = new JSONArray(price_content);
			JSONObject jsonObject = jsonArray.getJSONObject(0);
			page.addField("price", jsonObject.getString("p"));
			
			//获取规格参数
			Object[] evaluateXPath = rootNode.evaluateXPath("//*[@id=\"product-detail-2\"]/table/tbody/tr");
			JSONArray paramjsonArray = new JSONArray();
			if(evaluateXPath.length>0){
				for (Object object : evaluateXPath) {
					TagNode trNode = (TagNode)object;
					//首先把tr中内容是空的标签过滤掉
					if(!("").equals(trNode.getText().toString().trim())){
						JSONObject jsonObj = new JSONObject();
						Object[] evaluateXPathTh = trNode.evaluateXPath("/th");
						if(evaluateXPathTh.length>0){
							TagNode thNode = (TagNode)evaluateXPathTh[0];
							jsonObj.put("name", "");
							jsonObj.put("value", thNode.getText().toString());
						}else{
							Object[] evaluateXPathTd = trNode.evaluateXPath("/td");
							TagNode tdNode1 = (TagNode)evaluateXPathTd[0];
							TagNode tdNode2 = (TagNode)evaluateXPathTd[1];
							jsonObj.put("name", tdNode1.getText().toString());
							jsonObj.put("value", tdNode2.getText().toString());
						}
						paramjsonArray.put(jsonObj);
					}
				}
			}
			page.addField("spec", paramjsonArray.toString());
		} catch (XPatherException e) {
			System.out.println("解析失败。。。");
		}
	}

}
