
scalaVersion := "2.12.4"

name := "hello-world"
organization := "ch.epfl.scala"
version := "1.0"

resolvers += "confluent" at "http://packages.confluent.io/maven/"


libraryDependencies += "org.apache.kafka" % "kafka-clients" % "1.1.0"
libraryDependencies += "org.apache.kafka" % "connect-api" % "0.9.0.0"

libraryDependencies += "io.confluent" % "kafka-avro-serializer" % "3.0.0"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.7"
