package com.zhiyou.bd14

import com.zhiyou.bd14.result.HBaseUtils
import org.apache.hadoop.hbase.client.Put
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Duration, StreamingContext}

object SaveWCToHBase {

val conf = new SparkConf()
    .setAppName("统计字数, 写入HBase")
    .setMaster("local[*]")
  val ssc = new StreamingContext(conf, Duration(3000))

  ssc.checkpoint("/user/temp/streamingcheckpoint")

  def allSumWordCount() = {
    val dstream = ssc.socketTextStream("master", 9999)

    //wc微批次计算
    val result = dstream.flatMap(_.split("\\s"))
      .map((_, 1))
      .reduceByKey(_ + _)
    //获取并更新状态
    val state =
      result.updateStateByKey[Int]((nowBat: Seq[Int], s: Option[Int]) => {
        s match {
          case Some(value) => Some(value + nowBat.sum)
          case None => Some(nowBat.sum)
        }
      })

    state.print()

    state.foreachRDD((rdd, time) => {
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
  }

  def main(args: Array[String]): Unit = {
    allSumWordCount()
    ssc.start()
    ssc.awaitTermination()
  }


























}
