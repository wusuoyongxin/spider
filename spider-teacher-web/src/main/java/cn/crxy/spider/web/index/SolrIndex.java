package cn.crxy.spider.web.index;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.crxy.spider.web.domain.Goods;
import cn.crxy.spider.web.utils.HbaseUtils;
import cn.crxy.spider.web.utils.MyDateUtils;
import cn.crxy.spider.web.utils.SleepUtils;
import cn.crxy.spider.web.utils.SolrUtil;
import cn.crxy.spider.web.utils.RedisUtils;

/**
 * 建立商品索引，
 * 从redis中查询需要建索引的商品ID
 * 然后查询hbase，建立索引
 * 如果建立索引的时候出现异常，则把当前商品id重新放入索引表中
 *
 */
public class SolrIndex{
	static final Logger logger = LoggerFactory.getLogger(SolrIndex.class);
	private static final String SOLR_INDEX = "solr_index";
	static RedisUtils redis = new RedisUtils();
	/**
	 * 建立索引
	 */
	public static void doIndex(){
		String goodsId = "";
		try {
			HbaseUtils hbaseUtils = new HbaseUtils();
			goodsId = redis.poll(SOLR_INDEX);
			while (!Thread.currentThread().isInterrupted()) {
				if(StringUtils.isNotBlank(goodsId)){
					Goods goods = hbaseUtils.get(HbaseUtils.TABLE_NAME, goodsId);
					if(goods==null){
						logger.error("id为{}的商品索引建立失败!原因：没有在hbase数据库中查询到数据", goodsId);
					}else{
						if(goodsId.startsWith("jd_")){
							goods.setFrom("京东");
						}else if(goodsId.startsWith("yx_")){
							goods.setFrom("易迅");
						}else{
							goods.setFrom("未知");
						}
						SolrUtil.addIndex(goods);
					}
					goodsId = redis.poll(SOLR_INDEX);
				}else{
					System.out.println("暂时没有需要索引的数据,休息一会");
					SleepUtils.sleep(5000);
				}
			}
		} catch (Exception e) {
			logger.error("id为{}的商品索引建立失败!{}", goodsId, e);
			redis.add(SOLR_INDEX, goodsId);
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		doIndex();
	}
}
