package com.zhiyou.bd14.result

import java.sql.DriverManager

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object StreamingSaveToMysql {

  val conf = new SparkConf()
    .setMaster("local[*]")
    .setAppName("save to mysql")

  val ssc = new StreamingContext(conf,Seconds(5))

  def main(args: Array[String]): Unit = {
    //获取流数据
    val dstream = ssc.socketTextStream("master",9999)
    val result = dstream.flatMap(_.split("\\s"))
      .map((_,1))
      .reduceByKey(_+_)
    //把数据结果保存到mysql中
    result.foreachRDD((rdd,time) => {
      val timestamp = time.milliseconds
      //rdd把数据保存到mysql中的过程
      rdd.foreachPartition(wcs => {
        Class.forName("com.mysql.jdbc.Driver")
        val connection =
          DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/bigdata14","root","root")
        val sql = "insert into streaming_wc (ts,word,w_count) values (?,?,?)"
        val preparedStatement = connection.prepareStatement(sql)
        for(record <- wcs){
          preparedStatement.setLong(1,timestamp)
          preparedStatement.setString(2,record._1)
          preparedStatement.setInt(3,record._2)
          preparedStatement.addBatch()
        }
        preparedStatement.executeBatch()
        connection.close()
      })
    })

    ssc.start()
    ssc.awaitTermination()
  }



}
