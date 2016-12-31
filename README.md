# HazelCast client

As part of integrating real-time Kafka topics and sinking data using Kafka-Connect
into HazelCast - we had a need run integration tests against real infrastructures.

This Scala client code adds listeners on `Reliable Topics` and `Ring Buffers` and
prints out records, once such are sinked inside HazelCast

## Build

    sbt assembly
    java -jar target/scala-2.11/hazelcast-client.jar -host cloudera03.landoop.com:40105 -user dev -pass ****** -query sink-index

