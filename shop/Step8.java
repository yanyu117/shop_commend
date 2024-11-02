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
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
yarn jar shop_rem-1.0-SNAPSHOT.jar com.briup.shop.Step8 -D one=logs/2024-10-22.log -D two=step7_rs -D output=step8_rs
 */
public class Step8 extends Configured implements Tool {
    //初始文件，找购买行为
    static class Step8_0Mapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {
        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, DoubleWritable>.Context context) throws IOException, InterruptedException {
            String[] strs = value.toString().split(",");
            if (strs.length != 4) return;
            if ("paySuccess".equals(strs[2])) {
                Text ko = new Text(strs[0] + ":" + strs[1]);
                context.write(ko, new DoubleWritable(1));
            }
        }
    }

    static class Step8_7Mapper extends Mapper<Text, DoubleWritable, Text, DoubleWritable> {
        @Override
        protected void map(Text key, DoubleWritable value, Mapper<Text, DoubleWritable, Text, DoubleWritable>.Context context) throws IOException, InterruptedException {
            context.write(key, value);
        }
    }

    static class Step8Reducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {
//        private Connection conn;
//        private PreparedStatement ps;
//        private int num = 1;

//        @Override
//        protected void setup(Reducer<Text, DoubleWritable, Text, DoubleWritable>.Context context) throws IOException, InterruptedException {
//            try {
//                Class.forName("com.mysql.cj.jdbc.Driver");
//                conn = DriverManager.getConnection(
//                        "jdbc:mysql://192.168.10.11:3306/shops", "root", "root");
//                //执行前 还需要执行删除表中数据
//                //......
//                String sql1 = "delete from t_recommend_shop";
//                String sql = "insert into t_recommend_shop(user_id,recommend_value,shop_id) values(?,?,?)";
//                ps = conn.prepareStatement(sql1);
//                ps.executeBatch();
//                ps = conn.prepareStatement(sql);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        @Override
        protected void reduce(Text key, Iterable<DoubleWritable> values, Reducer<Text, DoubleWritable, Text, DoubleWritable>.Context context) throws IOException, InterruptedException {
            Iterator<DoubleWritable> iter = values.iterator();
            DoubleWritable vo = iter.next();
            if (!iter.hasNext()) {
//                String[] us = key.toString().split("[:]");
//                int uid = Integer.parseInt(us[0]);
//                int shodId = Integer.parseInt(us[1]);
//                try {
//                    ps.setInt(1, uid);
//                    ps.setDouble(2, vo.get());
//                    ps.setInt(3, shodId);
//                    ps.addBatch();
//                    if (num % 200 == 0) {
//                        ps.executeBatch();
//                    }
//                    num++;
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
                context.write(key, vo);
            }

        }

//        @Override
//        protected void cleanup(Reducer<Text, DoubleWritable, Text, DoubleWritable>.Context context) throws IOException, InterruptedException {
//            try {
//                ps.executeBatch();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    if (ps != null) ps.close();
//                    if (conn != null) conn.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = getConf();
        String step8_in1 = "shop";
        String step8_in2 = "step7_rs";
        String step8_out = "step8_rs";

        FileSystem fs = FileSystem.get(conf);
        Path outPath_8 = new Path(step8_out);
        if (fs.exists(outPath_8)) {
            fs.delete(outPath_8, true);
        }
        Job job = Job.getInstance(conf);
        job.setJobName("Step8");
        job.setJarByClass(this.getClass());

        //多个map 参1 job作业 参2 读取文件路径，参3 读取文件的处理器，参4 map程序
        MultipleInputs.addInputPath(job, new Path(step8_in1), TextInputFormat.class, Step8_0Mapper.class);
        MultipleInputs.addInputPath(job, new Path(step8_in2), SequenceFileInputFormat.class, Step8_7Mapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(DoubleWritable.class);


        job.setReducerClass(Step8Reducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);

        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileOutputFormat.setOutputPath(job, new Path(step8_out));
        return job.waitForCompletion(true) ? 0 : -1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new Step8(), args));
    }
}
