package com.landoop.hazelcast

import com.hazelcast.core._
import com.hazelcast.config._

// Spins up a local instance of hazelcast that can be subsequently be used
object LocalServer extends App {

  val cfg = new Config()
  val instance = Hazelcast.newHazelcastInstance(cfg)
  val mapCustomers: java.util.Map[Integer, String] = instance.getMap("customers")
  mapCustomers.put(1, "Joe")
  mapCustomers.put(2, "Ali")
  mapCustomers.put(3, "Avi")

  println("Customer with key 1: " + mapCustomers.get(1))
  println("Map Size:" + mapCustomers.size())

  val queueCustomers: java.util.Queue[String] = instance.getQueue("customers")
  queueCustomers.offer("Tom")
  queueCustomers.offer("Mary")
  queueCustomers.offer("Jane")

  println("First customer: " + queueCustomers.poll())
  println("Second customer: " + queueCustomers.peek())
  println("Queue size: " + queueCustomers.size())

}
