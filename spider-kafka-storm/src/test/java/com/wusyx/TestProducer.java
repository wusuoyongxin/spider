package com.wusyx;

import java.util.Properties;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class TestProducer {

	public static void main(String[] args) {

		Properties originalProps = new Properties();
		//broker
		originalProps.put("metadata.broker.list", "192.168.1.172:9092");
		//把数据序列化到broker
		originalProps.put("serializer.class", "kafka.serializer.StringEncoder");
		originalProps.put("request.required.acks", "1");
		Producer<String, String> producer = new Producer<String, String>(new ProducerConfig(originalProps ));
		for(int j = 0; j < 5; j++) {
			producer.send(new KeyedMessage<String, String>("job9", null, j+""));
		}
		producer.close();
	}

}
