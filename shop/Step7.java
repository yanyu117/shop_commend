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
yarn jar shop_rem-1.0-SNAPSHOT.jar com.briup.shop.Step7 -D input=step6_rs -D output=step7_rs
 */
public class Step7 extends Configured implements Tool {
    static class Step7Mapper extends Mapper<Text,DoubleWritable,Text,DoubleWritable>{
        @Override
        protected void map(Text key, DoubleWritable value, Mapper<Text, DoubleWritable, Text, DoubleWritable>.Context context) throws IOException, InterruptedException {
            context.write(key,value);
        }
    }


    static class Step7Reducer extends Reducer<Text,DoubleWritable,Text, DoubleWritable>{
        @Override
        protected void reduce(Text key, Iterable<DoubleWritable> values, Reducer<Text, DoubleWritable, Text, DoubleWritable>.Context context) throws IOException, InterruptedException {
            double sum = 0;
            for (DoubleWritable val : values){
                sum += val.get();
            }
            context.write(key,new DoubleWritable(sum));
        }
    }
    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = getConf();
        String step7_in = "step6_rs";
        String step7_out = "step7_rs";

        FileSystem fs = FileSystem.get(conf);
        Path outPath_7 = new Path(step7_out);
        if (fs.exists(outPath_7)){
            fs.delete(outPath_7,true);
        }
        Job job = Job.getInstance(conf);
        job.setJobName("Step7");
        job.setJarByClass(this.getClass());

        job.setMapperClass(Step7Mapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(DoubleWritable.class);

        job.setReducerClass(Step7Reducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);

        job.setInputFormatClass(SequenceFileInputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileInputFormat.addInputPath(job,new Path(step7_in));
        SequenceFileOutputFormat.setOutputPath(job,new Path(step7_out));
        return job.waitForCompletion(true)?0:-1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new Step7(),args));
    }
}