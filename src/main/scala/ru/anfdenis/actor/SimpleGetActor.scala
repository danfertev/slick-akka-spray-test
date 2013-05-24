package ru.anfdenis.actor

import akka.actor.Actor

import scala.slick.driver.PostgresDriver.simple._

import Database.threadLocalSession
import ru.anfdenis.db._
import ru.anfdenis.InitData

/**
 * Denis Anfertev
 * 23.05.13 14:14
 */
class SimpleGetActor extends Actor {

  import SimpleGetActor._

  def receive: Receive = {
    case i: Int => {
      db withSession {
        val result = (for (c <- Clients if c.id === i) yield c).list
        if (result.length != 1)
          sender ! akka.actor.Status.Failure(new RuntimeException("Data error"))
        else
          sender ! result.head
      }
    }
  }
}

object SimpleGetActor {
  lazy val db = InitData.db
}