package com.wusyx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;

public class TestConsumer {

	public static void main(String[] args) {
		Properties originalProps = new Properties();
		originalProps.put("zookeeper.connect", "192.168.1.172:2181");
		originalProps.put("group.id", "234");
		originalProps.put("serializer.class", "kafka.serializer.StringEncoder");
		ConsumerConnector consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(originalProps ));
		
		
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put("job9", 1);
		StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
		StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());
		Map<String, List<KafkaStream<String, String>>> topicMessageStreams = consumer.createMessageStreams(topicCountMap , keyDecoder, valueDecoder);
		KafkaStream<String, String> kafkaStream = topicMessageStreams.get("job9").get(0);
		ConsumerIterator<String, String> iterator = kafkaStream.iterator();
		while(iterator.hasNext()){
			MessageAndMetadata<String, String> next = iterator.next();
			System.out.println(next.message());
		}
	}

}
