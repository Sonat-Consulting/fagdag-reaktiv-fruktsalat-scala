package no.sonat.fruktsalat

import akka.actor.ActorSystem
import org.slf4j.LoggerFactory

import ListOfStuff._

object ListOfStuff {
  val stuffWeNeed = List("orange", "apple", "banana")
}

trait FruitApp extends App with FruitAppStuff {

}

trait FruitAppStuff {
  val log = LoggerFactory.getLogger(getClass)
  val system = ActorSystem.create("FruitApp")
  val client:Client = new ClientImpl(system, false)
}

// For use in Java:
abstract class FruitJavaApp extends FruitAppStuff {

  import scala.collection.JavaConversions._



  def getSuffWeNeed():java.util.List[String] = {
    stuffWeNeed
  }

  def getJavaClient():JavaClient = new JavaClientImpl(client)

  def toString(list:java.util.List[String]):String = {
    list.toList.toString()
  }

}


