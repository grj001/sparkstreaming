package com.zhiyou.bd14

import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Duration, StreamingContext}

object KafkaDirectWordCount {

  val conf = new SparkConf()
    .setMaster("local[*]")
    .setAppName("用direct方法从Kafka获取数据")

  val ssc = new StreamingContext(conf,Duration(3000))

  def main(args: Array[String]): Unit = {
    val kafkaParams = Map("bootstrap.servers"->"master:9092,slaver1:9092,master:9093"
      , "key.deserializer"->"org.apache.kafka.common.serialization.StringDeserializer"
      , "value.deserializer"->"org.apache.kafka.common.serialization.StringDeserializer"
      , "group.id"->"kafkatest"
      , "enable.auto.commit"->"false")
    val topics = Set("forstreaming")
    val consumerStrategies =
      ConsumerStrategies.Subscribe[String,String](topics,kafkaParams)
    val kafkaDStream = KafkaUtils.createDirectStream(
      ssc
      , LocationStrategies.PreferConsistent
      , consumerStrategies)

    kafkaDStream.map(x => x.value())
      .flatMap(_.split("\\s"))
      .map((_,1))
      .reduceByKey(_+_)
      .print()

    ssc.start()
    ssc.awaitTermination()
  }
}
