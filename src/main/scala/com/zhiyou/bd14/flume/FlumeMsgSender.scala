package com.zhiyou.bd14.flume

import java.nio.charset.Charset

import org.apache.flume.api.{RpcClient, RpcClientFactory}
import org.apache.flume.event.EventBuilder

object FlumeMsgSender {

  val client = RpcClientFactory.getDefaultInstance("master",33333)

  def sendEvent(msg:String) = {
    val event = EventBuilder.withBody(msg,Charset.forName("UTF-8"))
    client.append(event)
  }

  def main(args: Array[String]): Unit = {
    (1 to 100).foreach(x => {
      sendEvent(s"hello flume $x")
    })
    client.close()
  }
}
