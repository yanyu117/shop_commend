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
yarn jar shop_rem-1.0-SNAPSHOT.jar com.briup.shop.Step4 -D input=step3_rs -D output=step4_rs
 */
public class Step4 extends Configured implements Tool {

    static class Step4Mapper extends Mapper<Text, Text,Text, IntWritable>{
        @Override
        protected void map(Text key, Text value, Mapper<Text, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {
            StringBuffer stb = new StringBuffer();
            stb.append(key.toString()).append(":").append(value.toString());
            context.write(new Text(stb.toString()),new IntWritable(1));
        }
    }

    static class Step4Reducer extends Reducer<Text,IntWritable,Text, IntWritable>{
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Reducer<Text, IntWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values){
                sum += val.get();
            }
            context.write(key,new IntWritable(sum));
        }
    }
    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = getConf();
        String step4_in = "step3_rs";
        String step4_out = "step4_rs";

        FileSystem fs = FileSystem.get(conf);
        Path outPath_4 = new Path(step4_out);
        if (fs.exists(outPath_4)){
            fs.delete(outPath_4,true);
        }
        Job job = Job.getInstance(conf);
        job.setJobName("Step4");
        job.setJarByClass(this.getClass());

        job.setMapperClass(Step4Mapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setReducerClass(Step4Reducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setInputFormatClass(SequenceFileInputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileInputFormat.addInputPath(job,new Path(step4_in));
        SequenceFileOutputFormat.setOutputPath(job,new Path(step4_out));
        return job.waitForCompletion(true)?0:-1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new Step4(),args));
    }
}