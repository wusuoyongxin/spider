package com.wusyx.spider.teach;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * Created by Wu on 2015/12/24.
 */
public class CommonPeopleTest {
    final static String INPUT_PATH = "hdfs://xiao110:9001/common_friends/in/";
    final static String OUTPUT_PATH = "hdfs://xiao110:9001/common_friends/out/";

    public static void main(String[] args) throws IOException, URISyntaxException, ClassNotFoundException, InterruptedException {

        Configuration conf = new Configuration();

        final FileSystem fileSystem = FileSystem.get(new URI(INPUT_PATH), conf);
        if (fileSystem.exists(new Path(OUTPUT_PATH))) {
            fileSystem.delete(new Path(OUTPUT_PATH), true);
        }

        Job job = Job.getInstance(conf, "Find friend");
        job.setJarByClass(CommonPeopleTest.class);

        job.setMapperClass(MyMapper.class);
        job.setReducerClass(MyReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(INPUT_PATH));
        FileOutputFormat.setOutputPath(job, new Path(OUTPUT_PATH));

        System.exit(job.waitForCompletion(true) ? 0 : 1);

    }

    static class MyMapper extends Mapper<LongWritable, Text, Text, Text> {

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //分割字符串
            StringTokenizer stringTokenizer = new StringTokenizer(value.toString());

            Text owner = new Text();//存放自己
            Set<String> set = new TreeSet<String>();//存放朋友
            owner.set(stringTokenizer.nextToken());
            while (stringTokenizer.hasMoreTokens()) {
                set.add(stringTokenizer.nextToken());
            }

            String[] friends = new String[set.size()];//朋友
            friends = set.toArray(friends);

            for (int i = 0; i < friends.length; i++) {
                for (int j = i + 1; j < friends.length; j++) {
                    String outputkey = friends[i] + friends[j];//朋友之间两两组合
                    context.write(new Text(outputkey), owner);//<朋友组合，自己>
                }
            }
        }
    }

    static class MyReducer extends Reducer<Text, Text, Text, Text> {

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            //以朋友组合作为key值，自己作为value值。
            String commonFriends = "";
            for (Text val : values) {
                if (commonFriends == "") {
                    commonFriends = val.toString();
                } else {
                    commonFriends = commonFriends + "--" + val.toString();
                }
            }
            context.write(key, new Text(commonFriends));
        }
    }
}