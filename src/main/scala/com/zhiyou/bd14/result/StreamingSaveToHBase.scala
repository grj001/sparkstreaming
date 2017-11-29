package com.zhiyou.bd14.result

import org.apache.hadoop.hbase.client.Put
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Duration, StreamingContext}

import scala.collection.mutable.ListBuffer

object StreamingSaveToHBase {

  val conf = new SparkConf()
    .setMaster("local[*]")
    .setAppName("save to hbase")

  val ssc = new StreamingContext(conf, Duration(5000))

  def main(args: Array[String]): Unit = {
    val dstream = ssc.socketTextStream("master", 9999)
    val result = dstream.flatMap(_.split("\\s"))
      .map((_, 1))
      .reduceByKey(_ + _)
    //使用foreachRDD通过对rdd的操作把数据保存到hbase中
    // create 'streaming_wc','i'
    // 时间戳作为rowkey, 表示批次, 然后word作为column
    // count 作为 value
    result.foreachRDD((rdd, time) => {
      val timestamp = time.milliseconds.toString
      rdd.foreachPartition(wcs => {
        val table = HBaseUtils.getTable("streaming_wc")
        //构建Put对象
        val putList = new java.util.ArrayList[Put]()
        for (wc <- wcs) {
          val put = new Put(timestamp.getBytes())
          put.addColumn("i".getBytes, wc._1.getBytes, wc._2.toString.getBytes)
          putList.add(put)
        }
        table.put(putList)
      })
    })
    ssc.start()
    ssc.awaitTermination()
  }
}
