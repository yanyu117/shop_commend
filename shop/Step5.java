package com.briup.shop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
/*
yarn jar shop_rem-1.0-SNAPSHOT.jar com.briup.shop.Step5 -D input=step4_rs -D output=step5_rs
 */

public class Step5 extends Configured implements Tool {

    static class Step5Mapper extends Mapper<Text, IntWritable,Text,Text>{
        StringBuffer stb = new StringBuffer();
        @Override
        protected void map(Text key, IntWritable value, Mapper<Text, IntWritable, Text, Text>.Context context) throws IOException, InterruptedException {
            String[] strs = key.toString().split(":");
            Text ko = new Text(strs[0]);
            Text vo = new Text(stb.append(strs[1]).append(":").append(value.get()).toString());
            context.write(ko,vo);
            stb.setLength(0);
        }
    }

    static class Step5Reducer extends Reducer<Text,Text,Text, Text>{
        StringBuffer stb = new StringBuffer();
        @Override
        protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, Text, Text>.Context context) throws IOException, InterruptedException {
            for (Text val : values){
                stb.append(val.toString()).append("#");
            }
            stb.setLength(stb.length()-1);
            context.write(key,new Text(stb.toString()));
            stb.setLength(0);
        }
    }
    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = getConf();
        String step5_in = "step4_rs";
        String step5_out = "step5_rs";

        FileSystem fs = FileSystem.get(conf);
        Path outPath_5 = new Path(step5_out);
        if (fs.exists(outPath_5)){
            fs.delete(outPath_5,true);
        }
        Job job = Job.getInstance(conf);
        job.setJobName("Step5");
        job.setJarByClass(this.getClass());

        job.setMapperClass(Step5Mapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setReducerClass(Step5Reducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setInputFormatClass(SequenceFileInputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileInputFormat.addInputPath(job,new Path(step5_in));
        SequenceFileOutputFormat.setOutputPath(job,new Path(step5_out));
        return job.waitForCompletion(true)?0:-1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new Step5(),args));
    }
}