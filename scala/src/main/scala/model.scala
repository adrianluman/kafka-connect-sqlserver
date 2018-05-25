package com.pinnacle.test

sealed trait Message
final case class SourceMessage(msg: String) extends Message {
  val text = msg
}
final case class SinkMessage(msg: String) extends Message {
  val text = msg
}
