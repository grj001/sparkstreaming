package com.zhiyou.bd14.flume

import org.apache.spark.SparkConf
import org.apache.spark.streaming.flume.FlumeUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object StreamingPollFlume {

  val conf = new SparkConf().setMaster("local[*]")
    .setAppName("streaming poll from flume")
  val ssc = new StreamingContext(conf, Seconds(5))

  def main(args: Array[String]): Unit = {
    //从flume中poll数据, 形成dstream
    val flumeDstream = FlumeUtils.createPollingStream(ssc,"master",9999)
    flumeDstream.map(x => new String(x.event.getBody.array()))
        .flatMap(x => x.split("\\s"))
      .map((_,1))
      .reduceByKey(_+_)
      .print()


    ssc.start()
    ssc.awaitTermination()
  }
}
