package no.sonat.fruktsalat.apps

import no.sonat.fruktsalat.FruitApp
import no.sonat.fruktsalat.ListOfStuff._

import scala.concurrent.Future

object AsyncApp extends FruitApp {
  import scala.concurrent.ExecutionContext.Implicits.global

  val futureIngredients = stuffWeNeed.map {
    whatWeNeed =>
      log.info(s"Starting to fetch $whatWeNeed")
      val futureToResult = client.getFruit(whatWeNeed)
      log.info(s"Now we're waiting for $whatWeNeed")
      futureToResult
  }

  log.info("Now we can wait for all ingredients to arrive, then do our work!")
  Future.sequence(futureIngredients) onSuccess {
    case ingredients => log.info(s"Our list is complete: $ingredients")
  }

  log.info(s"..Our job here is done..")
}
