package com.briup.shop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.join.CompositeInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;

/*
yarn jar shop_rem-1.0-SNAPSHOT.jar com.briup.shop.Step6 -D one=step2_rs -D two=step5_rs -D output=step6_rs
 */
public class Step6 extends Configured implements Tool {

    static class Step6Mapper extends Mapper<Text,Text,Sf,Text>{
        private String dir;

        @Override
        protected void setup(Mapper<Text, Text, Sf, Text>.Context context) throws IOException, InterruptedException {
            FileSplit fs = (FileSplit) context.getInputSplit();
            //拿数据片路径文件上一级文件的文件名
            dir = fs.getPath().getParent().getName();
        }

        @Override
        protected void map(Text key, Text value, Mapper<Text, Text, Sf, Text>.Context context) throws IOException, InterruptedException {
            String shopId = key.toString();
            Sf sf = new Sf();
            sf.setShopId(shopId);
            if ("step2_rs".equals(dir)){
                sf.setFlag(0);
            }else {
                sf.setFlag(1);
            }
            context.write(sf,value);
        }
    }

    static class Step6Reducer extends Reducer<Sf,Text,Text, DoubleWritable>{
        @Override
        protected void reduce(Sf key, Iterable<Text> values, Reducer<Sf, Text, Text, DoubleWritable>.Context context) throws IOException, InterruptedException {
            HashMap<String,Double> map1 = new HashMap<>();
            HashMap<String,Double> map2 = new HashMap<>();
            for (Text val : values){   //区别map1 2 的数据
                String line = val.toString();
                if (line.contains(",")){ //map1 是使用，分割的
                    String[] str_map1 = line.split(","); // str_map1[0] -> 8:1
                    for (String s1 : str_map1){
                        String[] str_1 = s1.split("[:]"); //str_1[0] -> 8  str_1[1] -> 1
                        //map1 -> k:8 v:1
                        map1.put(str_1[0],Double.parseDouble(str_1[1]));
                    }
                }else {    //map2 是使用#分割的
                    String[] str_map2 = line.split("#");  //str_map2[0] -> 10002:1
                    for (String s2 : str_map2){
                        String[] str_2 = s2.split("[:]"); //str_2[0] -> 10002 str_2[1] -> 1
                        //map2 -> k:10002 v:1
                        map2.put(str_2[0],Double.parseDouble(str_2[1]));
                    }
                }
            }
            map1.forEach((k1,v1)->{
               map2.forEach((k2,v2)->{
                   Text ko = new Text(k1+":"+k2);
                   DoubleWritable vo = new DoubleWritable(v1 * v2);
                   try {
                       context.write(ko,vo);
                   }catch (Exception e){
                       e.printStackTrace();
                   }
               });
            });
        }
    }
    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = getConf();
        String step6_in1 = "step2_rs";
        String step6_in2 = "step5_rs";
        String step6_out = "step6_rs";

        FileSystem fs = FileSystem.get(conf);
        Path outPath_6 = new Path(step6_out);
        if (fs.exists(outPath_6)){
            fs.delete(outPath_6,true);
        }

        Job job = Job.getInstance(conf);
        job.setJobName("Step6");
        job.setJarByClass(this.getClass());


        job.setMapperClass(Step6Mapper.class);
        job.setMapOutputKeyClass(Sf.class);
        job.setMapOutputValueClass(Text.class);

        job.setPartitionerClass(SfPartitioner.class);
        job.setGroupingComparatorClass(SfGroup.class);

        job.setReducerClass(Step6Reducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);
        job.setInputFormatClass(SequenceFileInputFormat.class);

        SequenceFileInputFormat.addInputPath(job,new Path(step6_in1));
        SequenceFileInputFormat.addInputPath(job,new Path(step6_in2));

        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileOutputFormat.setOutputPath(job,new Path(step6_out));
        return job.waitForCompletion(true)?0:-1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new Step6(),args));
    }
}

//分区
class SfPartitioner extends Partitioner<Sf,Text>{

    @Override
    public int getPartition(Sf key, Text value, int numPartitions) {
        return Math.abs(key.getShopId().hashCode()) * 127 % numPartitions;
    }
}
//分组
class SfGroup extends WritableComparator{
    public SfGroup(){
        super(Sf.class,true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        Sf k1 = (Sf) a;
        Sf k2 = (Sf) b;
        return k1.getShopId().compareTo(k2.getShopId());
    }
}
//map输出复合键
class Sf implements WritableComparable<Sf> {
    private String shopId;
    private int flag;

    @Override
    public int compareTo(Sf o) {
        int m = this.shopId.compareTo(o.shopId);
        if (m == 0){
            m = this.flag - o.flag;
        }
        return m;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(shopId);
        out.writeInt(flag);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.shopId = in.readUTF();
        this.flag = in.readInt();
    }

    public Sf() {
    }

    public Sf(String shopId, int flag) {
        this.shopId = shopId;
        this.flag = flag;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }


}