package no.sonat.fruktsalat

import ListOfStuff._
import no.sonat.fruktsalat.BlockingInARow._

import scala.concurrent.Future

object BlockingInARow extends FruitApp {

  val ingredients = stuffWeNeed.map {
    whatWeNeed =>

      val gotIt = client.getFruitNow(whatWeNeed)
      log.info(s"Now we have $gotIt")
      gotIt
  }

  log.info(s"Our list is complete: $ingredients")
}


