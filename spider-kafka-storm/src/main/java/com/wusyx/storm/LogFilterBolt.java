package com.wusyx.storm;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

/**
 * 接收kafakspout发射出来的数据进行处理
 * @author Administrator
 *
 */
public class LogFilterBolt extends BaseRichBolt {
	
	private static Logger logger = LoggerFactory.getLogger(LogFilterBolt.class);
	
	private OutputCollector collector;
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		try {
			byte[] binaryByField = input.getBinaryByField("bytes");
			String value = new String(binaryByField);
			this.collector.ack(input);
			logger.info("{}", value);
		} catch (Exception e) {
			this.collector.fail(input);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
	}

}
