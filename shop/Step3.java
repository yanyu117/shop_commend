package com.briup.shop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
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
import java.util.ArrayList;
import java.util.List;

/*
yarn jar shop_rem-1.0-SNAPSHOT.jar com.briup.shop.Step3 -D input=step1_rs -D output=step3_rs
 */
public class Step3 extends Configured implements Tool {

    static class Step3Mapper extends Mapper<Text, DoubleWritable,Text,Text>{
        @Override
        protected void map(Text key, DoubleWritable value, Mapper<Text, DoubleWritable, Text, Text>.Context context) throws IOException, InterruptedException {
            String[] strs = key.toString().split(",");
            context.write(new Text(strs[0]),new Text(strs[1]));
        }
    }

    static class Step3Reducer extends Reducer<Text,Text,Text, Text>{
        @Override
        protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, Text, Text>.Context context) throws IOException, InterruptedException {
            for (Text k : values){
                for (Text v : values){
                    context.write(k,v);
                }
            }
        }
    }
    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = getConf();
        String step3_in = "step1_rs";
        String step3_out = "step3_rs";

        FileSystem fs = FileSystem.get(conf);
        Path outPath_3 = new Path(step3_out);
        if (fs.exists(outPath_3)){
            fs.delete(outPath_3,true);
        }
        Job job = Job.getInstance(conf);
        job.setJobName("Step3");
        job.setJarByClass(this.getClass());

        job.setMapperClass(Step3Mapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setReducerClass(Step3Reducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setInputFormatClass(SequenceFileInputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileInputFormat.addInputPath(job,new Path(step3_in));
        SequenceFileOutputFormat.setOutputPath(job,new Path(step3_out));
        return job.waitForCompletion(true)?0:-1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new Step3(),args));
    }
}
