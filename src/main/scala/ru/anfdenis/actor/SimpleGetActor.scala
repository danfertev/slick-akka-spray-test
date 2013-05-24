package ru.anfdenis.actor

import akka.actor.Actor

import scala.slick.driver.PostgresDriver.simple._

import Database.threadLocalSession
import ru.anfdenis.db._

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
  lazy val db = Database.forURL("jdbc:postgresql://192.168.1.55:5432/users?user=postgres&password=RAPtor1234!", driver = "org.postgresql.Driver")
}