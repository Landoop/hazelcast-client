package com.landoop.hazelcast

import org.apache.spark.{SparkConf, SparkContext}
import java.net.URI

object Main extends App {

  //  sparkJobArgsParser.parse(args, SparkJobArgs()) match {
  //    case Some(parsedArgs) =>
  //  val environment = args.head

  println(s"Starting Main with args parsedArgs")
  val sparkConf = new SparkConf()
    .setAppName("financials.etl.solr")
    .setIfMissing("spark.master", "local[4]")
//    .setIfMissing("spark.driver.memory", "4g")
//    .setIfMissing("spark.executor.memory", "4g")
    .set("spark.sql.shuffle.partitions", "16")
    .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")

  val sparkContext = new SparkContext(sparkConf)

  val solrExport = new SolrExportPipeline("local_test", new URI("tmp/aurora_password.txt"), sparkContext)

  try {
    solrExport.run()
  } finally {
    sparkContext.stop()
  }

  //    case None =>
  //      sys.error("Invalid args")
  //  } 20:05:57 WA ..

}
