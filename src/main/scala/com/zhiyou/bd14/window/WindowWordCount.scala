package com.zhiyou.bd14.window

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Duration, StreamingContext}

object WindowWordCount {

  val conf = new SparkConf()
    .setMaster("local[*]")
    .setAppName("窗口操作")
  val ssc =
    new StreamingContext(conf, Duration(3000))

  ssc.checkpoint(
    "/user/temp/streamingcheckpoint")




  def wordCount() = {
    val dstream =
      ssc.socketTextStream("master", 9999)

    //先转换后开窗聚合
    val transaformation =
      dstream.flatMap(_.split("\\s"))
        .map((_, 1))
    //不指定滑动宽度, 默认会以微批次的宽度为计算的时间间隔
    //    val result =
    //      transaformation
    //        .reduceByKeyAndWindow(_+_,Duration(6000))

    //指定滑动宽度
    //    val result =
    //          transaformation
    //            .reduceByKeyAndWindow((x1:Int,x2:Int) => x1+x2
    //              , Duration(12000)
    //              , Duration(6000)
    //              , 2)

    val result =
    transaformation
      .reduceByKeyAndWindow((x1: Int, x2: Int) => x1 + x2
        , (c1, v2) => c1 - v2
        , Duration(12000)
        , Duration(6000)
        , 2)

    result.print()
    result.foreachRDD((k,v) => {
      println(k+"---"+v)
    })
  }




  def wordCount01() = {
    val dstream =
      ssc.socketTextStream("master", 9999)
    //先开窗再聚合
    val windowResult = dstream.window(Duration(12000),Duration(6000))
    windowResult.flatMap(_.split("\\s"))
      .map((_,1))
      .reduceByKey(_+_)
      .print()

    windowResult.print()
    windowResult.foreachRDD((k,v) => {
      println(k+"---"+v)
    })
  }




  def main(args: Array[String]): Unit = {
    wordCount()
    ssc.start()
    ssc.awaitTermination()
  }
}
