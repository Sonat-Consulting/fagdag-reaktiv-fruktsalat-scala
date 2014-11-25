package no.sonat.fruktsalat

import java.util.concurrent.{Future => JFuture, TimeUnit}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait JavaClient {
  def getFruit(name:String):JFuture[String]
  def getFruitNow(name:String):String

  def getFruit(name:String, amount:Int):JFuture[List[String]]
  def getFruitNow(name:String, amount:Int):List[String]
}


class JavaClientImpl(client:Client) extends JavaClient {

  override def getFruit(name: String): JFuture[String] = JFutureWrapper(client.getFruit(name))

  override def getFruitNow(name: String): String = client.getFruitNow(name)

  override def getFruitNow(name: String, amount: Int): List[String] = client.getFruitNow(name, amount)

  override def getFruit(name: String, amount: Int): JFuture[List[String]] = JFutureWrapper(client.getFruit(name, amount))
}

object JFutureWrapper {
  def apply[T](f:Future[T]) = new JFutureWrapper[T](f)
}

class JFutureWrapper[T](f:Future[T]) extends JFuture[T] {

  override def cancel(mayInterruptIfRunning: Boolean): Boolean = ???

  override def isCancelled: Boolean = false

  override def get(): T = Await.result(f, Duration("2 minutes"))

  override def get(timeout: Long, unit: TimeUnit): T = Await.result(f, Duration(timeout, unit))

  override def isDone: Boolean = f.isCompleted
}