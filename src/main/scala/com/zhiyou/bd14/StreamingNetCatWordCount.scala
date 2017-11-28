package com.zhiyou.bd14

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}


//从网络端口获取数据统计每个单词出现的次数
object StreamingNetCatWordCount {

  //如果使用local的话至少是local[2]
  // 如果只有一个县城,
  // 那么想成始终都在处理接收数据, 计算数据的过程就不会被被执行
  val conf = new SparkConf().setMaster("local[*]")
    .setAppName("spark streaming")
    //第二个参数是为批次的时间间隔, 使用sceonds minutes等类来制定
  val ssc = new StreamingContext(conf, Seconds(3))

  def test01() = {
    val dstream = ssc.socketTextStream("master",9999)
    val result = dstream.flatMap(x => {
      x.split("\\|")
    }).map(x => (x,1))
      .reduceByKey(_+_)
    result.print(20)
    ssc.start()
    ssc.awaitTermination()
  }

  def main(args: Array[String]): Unit = {
    test01()
  }

}
