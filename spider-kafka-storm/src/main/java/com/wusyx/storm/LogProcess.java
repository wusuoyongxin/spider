package com.wusyx.storm;

import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;

public class LogProcess {

    public static void main(String[] args) {

        TopologyBuilder topologyBuilder = new TopologyBuilder();
        BrokerHosts hosts = new ZkHosts("192.168.199.110:2181");//表示kafka的地址，可以指定zookeeper地址也可以
        String topic = "spider";//kafak中的主题
        String zkRoot = "/spider";//zk的一个根目录
        String id = "123";//是groupid
        SpoutConfig spoutConf = new SpoutConfig(hosts, topic, zkRoot, id);

        String spout_id = KafkaSpout.class.getSimpleName();
        String bolt_id = LogFilterBolt.class.getSimpleName();


        topologyBuilder.setSpout(spout_id, new KafkaSpout(spoutConf));
        topologyBuilder.setBolt(bolt_id, new LogFilterBolt()).shuffleGrouping(spout_id);

        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology("topology-name", new Config(), topologyBuilder.createTopology());


    }

}
