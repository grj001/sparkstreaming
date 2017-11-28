package com.zhiyou

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SpoolDirectoryWordCount {

  val conf = new SparkConf()
    .setMaster("local[*]")
    .setAppName("箭头in该文件获取新文件数据流")

  val scc = new StreamingContext(conf,Seconds(5))

  def monitorDirectory() = {
    val fileDstream = scc.textFileStream("/user/spark_streaming_listen")
    fileDstream.foreachRDD(x => {
      x.foreach(x => {
        println
      })
    })
    scc.start()
    scc.awaitTermination()
  }

  def main(args: Array[String]): Unit = {
    monitorDirectory()
  }


}
