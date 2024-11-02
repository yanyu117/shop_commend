package com.briup.shop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.db.DBOutputFormat;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;

import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
yarn jar recommend-jar-with-dependencies.jar com.briup.shop.Step10 -D input=step8_rs
 */
public class Step10 extends Configured implements Tool {

    static class Step10Mapper extends Mapper<Text, DoubleWritable,Text,DoubleWritable>{
        @Override
        protected void map(Text key, DoubleWritable value, Mapper<Text, DoubleWritable, Text, DoubleWritable>.Context context) throws IOException, InterruptedException {
            context.write(key, value);
        }
    }

    static class Step10Reducer extends Reducer<Text,DoubleWritable,ShopRecommend, NullWritable>{
        @Override
        protected void reduce(Text key, Iterable<DoubleWritable> values, Reducer<Text, DoubleWritable, ShopRecommend, NullWritable>.Context context) throws IOException, InterruptedException {
            String[] strs = key.toString().split(":");
            for (DoubleWritable val : values){
                ShopRecommend ko = new ShopRecommend(Long.parseLong(strs[0]),strs[1], val.get());
                context.write(ko,NullWritable.get());
            }
        }
    }
    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = getConf();
        //String input = conf.get("input");
        String step10_in = "step8_rs";
        //?useUnicode=true&amp;characterEncoding=utf8
        DBConfiguration.configureDB(conf, "com.mysql.cj.jdbc.Driver",
                "jdbc:mysql://192.168.10.11:3306/shops?serverTimezone=UTC&characterEncoding=utf-8",
                "root", "root");
        Job job = Job.getInstance(conf);
        job.setJobName("Step10");
        job.setJarByClass(this.getClass());

        job.setMapperClass(Step10Mapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(DoubleWritable.class);



        job.setReducerClass(Step10Reducer.class);
        job.setOutputKeyClass(ShopRecommend.class);
        job.setOutputValueClass(NullWritable.class);

        job.setInputFormatClass(SequenceFileInputFormat.class);
        job.setOutputFormatClass(DBOutputFormat.class);
        SequenceFileInputFormat.addInputPath(job,new Path(step10_in));
        DBOutputFormat.setOutput(job,"t_recommend_shop","user_id","shops_id","recommend_value");
        return job.waitForCompletion(true)?0:-1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new Step10(),args));
    }
}

class ShopRecommend implements DBWritable,Writable{

    private long user_id;
    private String shop_id;
    private double recommend_value;

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeLong(user_id);
        out.writeUTF(shop_id);
        out.writeDouble(recommend_value);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.user_id = in.readLong();
        this.shop_id = in.readUTF();
        this.recommend_value = in.readDouble();
    }

    @Override
    public void write(PreparedStatement statement) throws SQLException {
        statement.setLong(1,user_id);
        statement.setString(2,shop_id);
        statement.setDouble(3,recommend_value);
    }

    @Override
    public void readFields(ResultSet resultSet) throws SQLException {
        user_id = resultSet.getLong("user_id");
        shop_id = resultSet.getString("shops_id");
        recommend_value = resultSet.getDouble("recommend_value");
    }

    public ShopRecommend() {
    }

    public ShopRecommend(long user_id, String shop_id, double recommend_value) {
        this.user_id = user_id;
        this.shop_id = shop_id;
        this.recommend_value = recommend_value;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public double getRecommend_value() {
        return recommend_value;
    }

    public void setRecommend_value(double recommend_value) {
        this.recommend_value = recommend_value;
    }

    @Override
    public String toString() {
        return "ShopRecommend{" +
                "user_id=" + user_id +
                ", shop_id='" + shop_id + '\'' +
                ", recommend_value=" + recommend_value +
                '}';
    }
}