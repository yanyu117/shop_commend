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
/*
yarn jar shop_rem-1.0-SNAPSHOT.jar com.briup.shop.Step2 -D input=step1_rs -D output=step2_rs
 */

public class Step2 extends Configured implements Tool {

    static class Step2Mapper extends Mapper<Text, DoubleWritable,Text,Text>{
        @Override
        protected void map(Text key, DoubleWritable value, Mapper<Text, DoubleWritable, Text, Text>.Context context) throws IOException, InterruptedException {
            String[] strs = key.toString().split(",");
            String v = strs[0]+":"+value.toString();
            context.write(new Text(strs[1]),new Text(v));
        }
    }

    static class Step2Reducer extends Reducer<Text,Text,Text, Text>{
        StringBuffer stb = new StringBuffer();
        @Override
        protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, Text, Text>.Context context) throws IOException, InterruptedException {
            for (Text val : values){
                stb.append(val.toString()).append(",");
            }
            stb.setLength(stb.length()-1);
            context.write(key,new Text(stb.toString()));
            stb.setLength(0);
        }
    }
    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = getConf();
        String step2_in = "step1_rs";
        String step2_out = "step2_rs";

        FileSystem fs = FileSystem.get(conf);
        Path outPath_2 = new Path(step2_out);
        if (fs.exists(outPath_2)){
            fs.delete(outPath_2,true);
        }
        Job job = Job.getInstance(conf);
        job.setJobName("Step2");
        job.setJarByClass(this.getClass());

        job.setMapperClass(Step2Mapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);


        job.setReducerClass(Step2Reducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setInputFormatClass(SequenceFileInputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileInputFormat.addInputPath(job,new Path(step2_in));
        SequenceFileOutputFormat.setOutputPath(job,new Path(step2_out));
        return job.waitForCompletion(true)?0:-1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new Step2(),args));
    }
}
