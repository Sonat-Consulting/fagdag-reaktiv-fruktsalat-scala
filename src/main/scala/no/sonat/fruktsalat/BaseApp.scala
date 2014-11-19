package no.sonat.fruktsalat

import akka.actor.ActorSystem
import org.slf4j.LoggerFactory

import ListOfStuff._

object ListOfStuff {
  val stuffWeNeed = List("orange", "apple", "banana")
}

trait FruitApp extends App {
  val log = LoggerFactory.getLogger(getClass)
  val system = ActorSystem.create("FruitApp")
  val client:Client = new ClientImpl(system, false)
}


