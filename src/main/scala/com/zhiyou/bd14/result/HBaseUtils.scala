package com.zhiyou.bd14.result

import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.ConnectionFactory

object HBaseUtils {

  val conf = HBaseConfiguration.create()
  val connection = ConnectionFactory.createConnection(conf)

  //获取Table对象, 用来进行比哦啊的增删改查操作
  def getTable(tableName:String) = {
    connection.getTable(TableName.valueOf(tableName))

  }
}
