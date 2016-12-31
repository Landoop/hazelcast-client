package com.landoop.hazelcast

import com.hazelcast.Scala._
import com.hazelcast.client.HazelcastClient
import com.hazelcast.client.config.{ClientConfig, ClientNetworkConfig}
import com.hazelcast.core._
import com.hazelcast.security.UsernamePasswordCredentials
import scala.collection.JavaConversions._

// -host cloudera03.landoop.com:40105 -user dev -pass dev-pass -query sink-index
object Main extends App with ArgumentsSupport {

  val appArgs = parseArgumentsOrFail(args)
  val address = appArgs.host
  val user = appArgs.user
  val pass = appArgs.password
  val query = appArgs.query

  // Create a connection to remote server
  val config = new ClientConfig()
  config.setInstanceName("hazelcast-client")
  config.setProperty("hazelcast.logging.type", "log4j")
  val credentials = new UsernamePasswordCredentials(user, pass)
  config.setCredentials(credentials)
  val network: ClientNetworkConfig = config.getNetworkConfig
  network.setAddresses(List(address))
  val instance = HazelcastClient.newHazelcastClient(config)

  // Listen to reliable-topic as provided in the query
  listenReliableTopic(query)





  def publishToReliableTopic(topicName: String) = {
    val reliableTopic: ITopic[String] = instance.getReliableTopic(topicName)
    reliableTopic.publish("1st message published")
    reliableTopic.publish("2nd message published")
  }

  def listenReliableTopic(topicName: String) = {
    val reliableTopic: ITopic[String] = instance.getReliableTopic(topicName)
    println(s"Adding a listener to reliable-topic: [$topicName]")
    reliableTopic.onSeqMessage(startFrom = 0, gapTolerant = true) {
      case (seq, msg) =>
        println(s"Reliable Topic [$topicName] with seq [$seq]  msg = " + new String(msg.getMessageObject.asInstanceOf[Array[Byte]]))
    }
  }

  def showMap(mapName: String) = {
    val map = instance.getMap(mapName)
    println("Map Size: " + map.size())
  }

  def showList(listName: String) = {
    val list = instance.getList(listName)
    println("List Size: " + list.size())
  }

  def showMultiMap(multimapName: String) = {
    val multimap = instance.getMultiMap(multimapName)
    println("Multi Map Size: " + multimap.size())
  }

  def showQueue(queueName: String) = {
    val queue = instance.getQueue(queueName)
    println("Queue Size: " + queue.size())
  }

}
