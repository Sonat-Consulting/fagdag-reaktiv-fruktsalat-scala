package no.sonat.fruktsalat

import akka.actor.ActorSystem
import org.slf4j.LoggerFactory
import spray.client.pipelining._
import spray.http.{HttpResponse, HttpRequest}
import spray.json.{DefaultJsonProtocol, JsonFormat}
import spray.util._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Promise, Future}
import scala.util.{Failure, Success}

case class FruitList(fruits:List[String])
//case class FruitList(fruits:String)

trait Client {
  def getFruit(name:String):Future[String]
  def getFruitNow(name:String):String

  def getFruit(name:String, amount:Int):Future[List[String]]
  def getFruitNow(name:String, amount:Int):List[String]
}

object Client {

  def apply() = new ClientImpl(ActorSystem.create("ClientImpl"))
  def apply(shouldFail:Boolean) = new ClientImpl(ActorSystem.create("ClientImpl"), shouldFail)
}



class ClientImpl(actorSystem:ActorSystem, shouldFail:Boolean = true) extends Client {

  val log = LoggerFactory.getLogger(getClass)
  //val baseUrl = "http://fruityfruit.azurewebsites.net/fruit"
  val baseUrl = "http://ec2-54-187-76-112.us-west-2.compute.amazonaws.com:9000/fruit"

  implicit val system = actorSystem
  import scala.concurrent.ExecutionContext.Implicits.global

  import spray.json._
  import DefaultJsonProtocol._
  val pipeline = sendReceive ~> unmarshal[String]

  override def getFruit(name:String, amount:Int):Future[List[String]] = {
    var url = s"$baseUrl/$name/$amount"

    log.debug(s"fetching $url")

    if (!shouldFail) {
      url = url + "?errorRateInPercent=0"
    }
    val responseFuture = pipeline(Get(url))

    val p = Promise[List[String]]

    responseFuture onComplete {
      case Success(s) =>
        val list = s.parseJson.convertTo[List[String]]
        log.debug(s"Got the result: $list")
        p.success(list)
      case Failure(error) =>
        p.failure(error)
    }

    p.future
  }

  override def getFruitNow(name: String, amount: Int): List[String] = {
    Await.result(getFruit(name, amount), Duration("20s"))
  }

  override def getFruit(name: String): Future[String] = getFruit(name, 1).map( list => list(0))

  override def getFruitNow(name: String): String = getFruitNow(name,1)(0)
}
