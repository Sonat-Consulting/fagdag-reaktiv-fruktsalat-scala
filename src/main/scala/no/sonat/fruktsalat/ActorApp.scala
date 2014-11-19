package no.sonat.fruktsalat

import akka.actor.Actor.Receive
import akka.actor.{Props, Actor, ActorLogging}
import ListOfStuff._

object ActorApp extends FruitApp {

  log.info("Start our fruitSalatActor")
  val fruitSalatActor = system.actorOf(Props(new FruitSalatActor(client)), "fruitSalatActor")

  log.info("Tell it to make our salat")

  fruitSalatActor ! MakeFruitSalat(stuffWeNeed)

  log.info("Done with this work...")

  // Make another one??
  Thread.sleep(300)
  fruitSalatActor ! MakeFruitSalat(List("SomeSpecialFruit.."))
  Thread.sleep(3000)
  fruitSalatActor ! MakeFruitSalat(List("SomeSpecialFruit.."))

}

case class MakeFruitSalat(ingredients:List[String])
case class GotOneIngredient(name:String)

class FruitSalatActor(client:Client) extends Actor with ActorLogging {

  implicit val ec = context.dispatcher

  var isMaking = false
  var whatWeNeed = List[String]()
  var whatWeHave = List[String]()

  override def receive: Receive = {

    case MakeFruitSalat(ingredients) => {

      if (isMaking) {
        log.warning("Cannot make another fruitSalat now - still working on the previous one..")

      } else {

        log.info(s"We're going to make a fruitSalat of: $ingredients. Getting what we need..")
        whatWeNeed = ingredients
        isMaking = true

        // For all ingredients, fetch it, and send the result back to us when it is ready
        ingredients.foreach {
          name => client.getFruit(name).onSuccess {
            case gotIt => {
              // Send it back to the actor
              self ! GotOneIngredient(gotIt)
            }
          }
        }
      }
    }

    case GotOneIngredient(name) => {
      log.info(s"Got one ingredient: $name")
      whatWeHave = whatWeHave :+ name

      log.info("We now have {} of {} ingredients", whatWeHave.size, whatWeNeed.size)

      if (whatWeHave.size >= whatWeNeed.size) {
        log.info(s"** We now have all we need!! Making the fruitSalat!!!")

        // since we're done, we can clean up and be ready to make another one
        isMaking = false
        whatWeHave = List()
        whatWeNeed = List()
      }


    }
  }
}
