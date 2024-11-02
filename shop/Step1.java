package com.briup.shop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
/*
yarn jar shop_rem-1.0-SNAPSHOT.jar com.briup.shop.Step1 -D input=logs/2024-10-22.log -D output=step1_rs
 */

public class Step1 extends Configured implements Tool {

    static class Step1Mapper extends Mapper<LongWritable, Text,Text,DoubleWritable>{
        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, DoubleWritable>.Context context) throws IOException, InterruptedException {
            String[] val = value.toString().split(",");
            if (val.length!=4) return;
            Text ko = new Text(val[0]+","+val[1]);
            double num = 0;
            if ("showProduct".equals(val[2])){
                num = 0.05;
            } else if ("addCart".equals(val[2])){
                num = 0.15;
            }else if ("createOrder".equals(val[2])){
                num = 0.3;
            }else if ("addCollect".equals(val[2])){
                num = 0.1;
            }else {
                num = 0.4;
            }
            DoubleWritable vo = new DoubleWritable(num);
            context.write(ko,vo);
        }
    }

    static class Step1Reducer extends Reducer<Text,DoubleWritable,Text, DoubleWritable>{
        @Override
        protected void reduce(Text key, Iterable<DoubleWritable> values, Reducer<Text, DoubleWritable, Text, DoubleWritable>.Context context) throws IOException, InterruptedException {
            double sum = 0;
            for(DoubleWritable val :values){
                sum += val.get();
            }
            context.write(key,new DoubleWritable(sum));
        }
    }
    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = getConf();
        String step1_in = "shop";
        String step1_out = "step1_rs";

        FileSystem fs = FileSystem.get(conf);
        Path outPath_1 = new Path(step1_out);
        if (fs.exists(outPath_1)){
            fs.delete(outPath_1,true);
        }
        Job job = Job.getInstance(conf);
        job.setJobName("Step1");
        job.setJarByClass(this.getClass());

        job.setMapperClass(Step1Mapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(DoubleWritable.class);


        job.setReducerClass(Step1Reducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        TextInputFormat.addInputPath(job,new Path(step1_in));
        SequenceFileOutputFormat.setOutputPath(job,new Path(step1_out));
        return job.waitForCompletion(true)?0:-1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new Step1(),args));
    }
}
