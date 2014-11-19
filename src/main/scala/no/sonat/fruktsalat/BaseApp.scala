package no.sonat.fruktsalat

import org.slf4j.LoggerFactory

import ListOfStuff._

object ListOfStuff {
  val stuffWeNeed = List("orange", "apple", "banana")
}

trait FruitApp extends App {
  val log = LoggerFactory.getLogger(getClass)
  val client = Client(false)
}


