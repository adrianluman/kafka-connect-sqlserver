package com.pinnacle.test

import grizzled.slf4j.Logging

// import com.pinnacle.test.BasicConsumer

// object Main extends App with Logging{
//   info("Hello, world")
//   val consumer = BasicConsumer("localhost:2181", "1", "10")

// }

import java.util
import scala.collection.JavaConverters._

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.avro.generic.GenericData.Record
import org.apache.avro.generic.GenericData._
import io.confluent.kafka.serializers._
import play.api.libs.json._

object Main extends App {

  val TOPIC="dinamis-timestamp-ReksaNAV"

  import java.util.Properties
  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer")
  // props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("schema.registry.url", "http://localhost:8081");
  props.put("group.id", "asdf")
  props.put("auto.offset.reset", "earliest")
  props.put("enable.auto.commit", "false")
  // props.put("specific.avro.reader", "true");
  // props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");

  val consumer = new KafkaConsumer[String, String](props)

  consumer.subscribe(util.Collections.singletonList(TOPIC))
  while(true){
    val records = consumer.poll(100)
    // println(consumer)
    // println(records)

    // records.records(TOPIC).iterator().toList.foreach { record =>
    //   println(s"|||${record.value}")
    // }

    val asdf: String = "asdf"
    // asdf.toString
    // println(asdf.get())

    // println(records.getClass)

    for (record<-records.asScala){
      // println(record.getClass)
      // println(record)
      // println(record.value.getClass)
      // println(record.value)
      // val value = record
      // println(record.key.getClass)
      // println(record.getClass)
      // println(record.value("CloseDate"))
      // println((record.value).getClass)
      // val value = record.value
      // println(record.value.getClass)
      // println(record.value.toString)
      // println(value.get(0))
      // println(value.key)
      // println(value.topic)
      // (record.value).compareTo(record.value)
      // println(record.get("value").toString)
      // println(record.value.toString)
      // val data = collection.mutable.Map[String, Object]()
      // // data.put("partition", record.partition)
      // // data.put("offset", record.offset)
      // data.put("value", record.value)
      // // println(data)
      // data foreach {case (key, value) => println (key + "-->" + value.toString)}
      // println(data("value").toString)
      // val json: JsValue = Json.parse(data("value").toString)
      println(record.value)
      // val asdf: Object = record.value
      // println(asdf.toString)
      // val json: JsValue = Json.parse(asdf.toString)
    }
  }
}
