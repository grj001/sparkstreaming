package com.zhiyou.bd14

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Duration, StreamingContext}

object StreamingToAvg {
  //  输入流数据, 接受流数据
  //  流数据中每一条只有一个数组
  //  统计出累计的实时的平均数

  val conf = new SparkConf()
    .setMaster("local[*]")
    .setAppName("窗口操作")
  val ssc = new StreamingContext(conf, Duration(3000))

  ssc.checkpoint("/user/temp/streamingcheckpoint")

  def avgNum() = {
    val dstream = ssc.socketTextStream("master", 9999)


        val result = dstream
          .flatMap(x => {
            //加入数组长度
            (x + "&" + x.split(",").length).split(",")
          })
          .map(x => {
            (1, x)
          })
          .reduceByKey((x1, x2) => {
            if (x2.contains("&")) {
              val v1 = x1.toInt
              val v2 = x2.split("&")(0).toInt
              val num = x2.split("&")(1).toInt
              val sum = v1 + v2
              "结果为" + (sum / num).toString
            } else {
              ((x1.toInt) + (x2.toInt)).toString
            }
          })

//    val result = dstream
//      .flatMap(x => {
//        //加入数组长度
//        x.split(",")
//      })
//      .map(x => {
//        (1, x.toInt)
//      }).updateStateByKey[Int]((nowBat: Seq[Int], s: Option[Int]) => {
//      println(nowBat.toString())
//      s match {
//        case Some(value) => Some(value + nowBat.sum)
//        case None => Some(nowBat.sum)
//      }
//    }).reduceByKeyAndWindow(Duration(12000),Duration(6000))


    result.print()
  }


  def main(args: Array[String]): Unit = {
    avgNum()
    ssc.start()
    ssc.awaitTermination()
  }
}
