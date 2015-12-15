package com.wusyx;

import java.util.Properties;

import org.junit.Test;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;

public class KafkaTest {

    @SuppressWarnings("static-access")
	@Test
    public void kafka() throws InterruptedException {
    	Properties originalProps = new Properties();
		originalProps.put("metadata.broker.list", "192.168.199.110:9092");
		originalProps.put("serializer.class", StringEncoder.class.getName());
		ProducerConfig producerConfig = new ProducerConfig(originalProps );
		Producer<String, String> producer = new Producer<String, String>(producerConfig);
		while(true){
			KeyedMessage<String, String> message = new KeyedMessage<String, String>("test", "www.wusyx.com" + System.currentTimeMillis());
			producer.send(message );
//			Thread.currentThread().sleep(1000);
		}
		
    }
}
