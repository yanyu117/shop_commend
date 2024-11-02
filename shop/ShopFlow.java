package com.briup.shop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.db.DBOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.jobcontrol.ControlledJob;
import org.apache.hadoop.mapreduce.lib.jobcontrol.JobControl;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.util.List;
/*
yarn jar shop_rem-1.0-SNAPSHOT.jar com.briup.shop.ShopFlow
 */
public class ShopFlow extends Configured implements Tool {
    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = getConf();

        String step1_in = "shop";
        String step1 = "step1_rs";
        String step2 = "step2_rs";
        String step3 = "step3_rs";
        String step4 = "step4_rs";
        String step5 = "step5_rs";
        String step6 = "step6_rs";
        String step7 = "step7_rs";
        String step8 = "step8_rs";
        DBConfiguration.configureDB(conf, "com.mysql.cj.jdbc.Driver",
                "jdbc:mysql://192.168.10.11:3306/shops?",
                "root", "root");

        FileSystem fs = FileSystem.get(conf);
        Path outPath_1 = new Path(step1);
        Path outPath_2 = new Path(step2);
        Path outPath_3 = new Path(step3);
        Path outPath_4 = new Path(step4);
        Path outPath_5 = new Path(step5);
        Path outPath_6 = new Path(step6);
        Path outPath_7 = new Path(step7);
        Path outPath_8 = new Path(step8);
        if (fs.exists(outPath_1)) fs.delete(outPath_1,true);
        if (fs.exists(outPath_2)) fs.delete(outPath_2,true);
        if (fs.exists(outPath_3)) fs.delete(outPath_3,true);
        if (fs.exists(outPath_4)) fs.delete(outPath_4,true);
        if (fs.exists(outPath_5)) fs.delete(outPath_5,true);
        if (fs.exists(outPath_6)) fs.delete(outPath_6,true);
        if (fs.exists(outPath_7)) fs.delete(outPath_7,true);
        if (fs.exists(outPath_8)) fs.delete(outPath_8,true);

        //step1
        Job job1 = Job.getInstance(conf);
        job1.setJobName("Step1");
        job1.setJarByClass(this.getClass());
        job1.setMapperClass(Step1.Step1Mapper.class);
        job1.setMapOutputKeyClass(Text.class);
        job1.setMapOutputValueClass(DoubleWritable.class);
        job1.setReducerClass(Step1.Step1Reducer.class);
        job1.setOutputKeyClass(Text.class);
        job1.setOutputValueClass(DoubleWritable.class);
        job1.setInputFormatClass(TextInputFormat.class);
        job1.setOutputFormatClass(SequenceFileOutputFormat.class);
        TextInputFormat.addInputPath(job1,new Path(step1_in));
        SequenceFileOutputFormat.setOutputPath(job1,new Path(step1));

        //step2
        Job job2 = Job.getInstance(conf);
        job2.setJobName("Step2");
        job2.setJarByClass(this.getClass());
        job2.setMapperClass(Step2.Step2Mapper.class);
        job2.setMapOutputKeyClass(Text.class);
        job2.setMapOutputValueClass(Text.class);
        job2.setReducerClass(Step2.Step2Reducer.class);
        job2.setOutputKeyClass(Text.class);
        job2.setOutputValueClass(Text.class);
        job2.setInputFormatClass(SequenceFileInputFormat.class);
        job2.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileInputFormat.addInputPath(job2,new Path(step1));
        SequenceFileOutputFormat.setOutputPath(job2,new Path(step2));

        //step3
        Job job3 = Job.getInstance(conf);
        job3.setJobName("Step3");
        job3.setJarByClass(this.getClass());
        job3.setMapperClass(Step3.Step3Mapper.class);
        job3.setMapOutputKeyClass(Text.class);
        job3.setMapOutputValueClass(Text.class);
        job3.setReducerClass(Step3.Step3Reducer.class);
        job3.setOutputKeyClass(Text.class);
        job3.setOutputValueClass(Text.class);
        job3.setInputFormatClass(SequenceFileInputFormat.class);
        job3.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileInputFormat.addInputPath(job3,new Path(step1));
        SequenceFileOutputFormat.setOutputPath(job3,new Path(step3));

        //step4
        Job job4 = Job.getInstance(conf);
        job4.setJobName("Step4");
        job4.setJarByClass(this.getClass());
        job4.setMapperClass(Step4.Step4Mapper.class);
        job4.setMapOutputKeyClass(Text.class);
        job4.setMapOutputValueClass(IntWritable.class);
        job4.setReducerClass(Step4.Step4Reducer.class);
        job4.setOutputKeyClass(Text.class);
        job4.setOutputValueClass(IntWritable.class);
        job4.setInputFormatClass(SequenceFileInputFormat.class);
        job4.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileInputFormat.addInputPath(job4,new Path(step3));
        SequenceFileOutputFormat.setOutputPath(job4,new Path(step4));

        //step5
        Job job5 = Job.getInstance(conf);
        job5.setJobName("Step5");
        job5.setJarByClass(this.getClass());
        job5.setMapperClass(Step5.Step5Mapper.class);
        job5.setMapOutputKeyClass(Text.class);
        job5.setMapOutputValueClass(Text.class);
        job5.setReducerClass(Step5.Step5Reducer.class);
        job5.setOutputKeyClass(Text.class);
        job5.setOutputValueClass(Text.class);
        job5.setInputFormatClass(SequenceFileInputFormat.class);
        job5.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileInputFormat.addInputPath(job5,new Path(step4));
        SequenceFileOutputFormat.setOutputPath(job5,new Path(step5));

        //step6
        Job job6 = Job.getInstance(conf);
        job6.setJobName("Step6");
        job6.setJarByClass(this.getClass());
        job6.setMapperClass(Step6.Step6Mapper.class);
        job6.setMapOutputKeyClass(Sf.class);
        job6.setMapOutputValueClass(Text.class);
        job6.setPartitionerClass(SfPartitioner.class);
        job6.setGroupingComparatorClass(SfGroup.class);
        job6.setReducerClass(Step6.Step6Reducer.class);
        job6.setOutputKeyClass(Text.class);
        job6.setOutputValueClass(DoubleWritable.class);
        job6.setInputFormatClass(SequenceFileInputFormat.class);
        SequenceFileInputFormat.addInputPath(job6,new Path(step2));
        SequenceFileInputFormat.addInputPath(job6,new Path(step5));
        job6.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileOutputFormat.setOutputPath(job6,new Path(step6));

        //step7
        Job job7 = Job.getInstance(conf);
        job7.setJobName("Step7");
        job7.setJarByClass(this.getClass());
        job7.setMapperClass(Step7.Step7Mapper.class);
        job7.setMapOutputKeyClass(Text.class);
        job7.setMapOutputValueClass(DoubleWritable.class);
        job7.setReducerClass(Step7.Step7Reducer.class);
        job7.setOutputKeyClass(Text.class);
        job7.setOutputValueClass(DoubleWritable.class);
        job7.setInputFormatClass(SequenceFileInputFormat.class);
        job7.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileInputFormat.addInputPath(job7,new Path(step6));
        SequenceFileOutputFormat.setOutputPath(job7,new Path(step7));

        //step8
        Job job8 = Job.getInstance(conf);
        job8.setJobName("Step8");
        job8.setJarByClass(this.getClass());
        //多个map 参1 job作业 参2 读取文件路径，参3 读取文件的处理器，参4 map程序
        MultipleInputs.addInputPath(job8,new Path(step1_in), TextInputFormat.class, Step8.Step8_0Mapper.class);
        MultipleInputs.addInputPath(job8,new Path(step7), SequenceFileInputFormat.class, Step8.Step8_7Mapper.class);
        job8.setMapOutputKeyClass(Text.class);
        job8.setMapOutputValueClass(DoubleWritable.class);
        job8.setReducerClass(Step8.Step8Reducer.class);
        job8.setOutputKeyClass(Text.class);
        job8.setOutputValueClass(DoubleWritable.class);
        job8.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileOutputFormat.setOutputPath(job8,new Path(step8));

        Job job9 = Job.getInstance(conf);
        job9.setJobName("Step10");
        job9.setJarByClass(this.getClass());
        job9.setMapperClass(Step10.Step10Mapper.class);
        job9.setMapOutputKeyClass(Text.class);
        job9.setMapOutputValueClass(DoubleWritable.class);
        job9.setReducerClass(Step10.Step10Reducer.class);
        job9.setOutputKeyClass(ShopRecommend.class);
        job9.setOutputValueClass(NullWritable.class);

        job9.setInputFormatClass(SequenceFileInputFormat.class);
        job9.setOutputFormatClass(DBOutputFormat.class);
        SequenceFileInputFormat.addInputPath(job9,new Path(step8));
        DBOutputFormat.setOutput(job9,"t_recommend_shop","user_id","shop_id","recommend_value");




        //配置作业的依赖
        ControlledJob c1 = new ControlledJob(conf); c1.setJob(job1);
        ControlledJob c2 = new ControlledJob(conf); c2.setJob(job2);
        ControlledJob c3 = new ControlledJob(conf); c3.setJob(job3);
        ControlledJob c4 = new ControlledJob(conf); c4.setJob(job4);
        ControlledJob c5 = new ControlledJob(conf); c5.setJob(job5);
        ControlledJob c6 = new ControlledJob(conf); c6.setJob(job6);
        ControlledJob c7 = new ControlledJob(conf); c7.setJob(job7);
        ControlledJob c8 = new ControlledJob(conf); c8.setJob(job8);
        ControlledJob c9 = new ControlledJob(conf); c9.setJob(job9);

        c2.addDependingJob(c1);
        c3.addDependingJob(c1);
        c4.addDependingJob(c3);
        c5.addDependingJob(c4);
        c6.addDependingJob(c2);
        c6.addDependingJob(c5);
        c7.addDependingJob(c6);
        c8.addDependingJob(c7);
        c9.addDependingJob(c8);


        //提交作业
        //将所有的可控作业加入作业组
        JobControl jobs = new JobControl("mergeMap");
        jobs.addJob(c1);
        jobs.addJob(c2);
        jobs.addJob(c3);
        jobs.addJob(c4);
        jobs.addJob(c5);
        jobs.addJob(c6);
        jobs.addJob(c7);
        jobs.addJob(c8);
        jobs.addJob(c9);
        //基于线程开启作业组的执行
        Thread t = new Thread(jobs);
        t.start();
        while (true){
            //获取正在运行的作业列表
            List<ControlledJob> list = jobs.getRunningJobList();
            for (ControlledJob c : list){
                //获取正在执行的作业输出日志
                c.getJob().monitorAndPrintJob();
            }
            //如果所有作业执行完成，结束while循环
            if (jobs.allFinished()) break;
        }

        return 0;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new ShopFlow(),args));
    }
}
