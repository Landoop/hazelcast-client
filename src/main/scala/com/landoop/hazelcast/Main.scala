package com.landoop.hazelcast

import com.hazelcast.config._
import com.hazelcast.Scala._
import com.hazelcast.Scala.client._
import com.hazelcast.client.HazelcastClient
import com.hazelcast.client.config.ClientConfig

object Main extends App {

  // Create a connection to remote server
  val server = "xxx.landoop.com:5701" //hazelcast/rest/cluster
  val clientConfig = new ClientConfig().addAddress(server)
  val client = HazelcastClient.newHazelcastClient(clientConfig)


  val map = client.getMap("customers")
  println("Map Size:" + map.size())

}
