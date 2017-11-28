package com.zhiyou.bd14

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.flume.FlumeUtils

object FlumePushStreaming {

  val conf = new SparkConf().setMaster("local[*]")
    .setAppName("data from flume")
  val ssc = new StreamingContext(conf, Seconds(5))

  def main(args: Array[String]): Unit = {
    //获取flume数据源
    val flumeDstream = FlumeUtils.createStream(ssc, "192.168.58.1",9999)
    //接受的数据计算word count
    flumeDstream.flatMap(x =>
      new String(
        x.event.getBody().array()
      ).split("\\s")
    ).map((_,1))
      .reduceByKey(_+_)
      .print()

    ssc.start()
    ssc.awaitTermination()

  }

}
