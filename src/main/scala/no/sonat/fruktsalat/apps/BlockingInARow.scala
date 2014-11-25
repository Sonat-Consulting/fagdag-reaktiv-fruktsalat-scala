package no.sonat.fruktsalat.apps

import no.sonat.fruktsalat.FruitApp
import no.sonat.fruktsalat.ListOfStuff._

object BlockingInARow extends FruitApp {

  val ingredients = stuffWeNeed.map {
    whatWeNeed =>

      log.info(s"Starting to fetch $whatWeNeed")
      val gotIt = client.getFruitNow(whatWeNeed)
      log.info(s"Now we have $gotIt")
      gotIt
  }

  log.info(s"Our list is complete: $ingredients")
}


