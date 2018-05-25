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

  val TOPIC="my-timestamp-user"

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

    for (record<-records.asScala){
      println(record.value)
    }
  }
}
