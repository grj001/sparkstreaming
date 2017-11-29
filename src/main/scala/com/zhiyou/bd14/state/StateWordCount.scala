package com.zhiyou.bd14.state

import org.apache.spark.streaming.{Duration, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object StateWordCount {
  val conf = new SparkConf()
    .setAppName("累计统计")
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
  }

  def main(args: Array[String]): Unit = {
    allSumWordCount()
    ssc.start()
    ssc.awaitTermination()
  }

}
