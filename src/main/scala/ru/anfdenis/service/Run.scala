package ru.anfdenis.service

import spray.routing.SimpleRoutingApp
import akka.pattern.ask
import ru.anfdenis.actor._
import akka.util.Timeout
import scala.concurrent.duration._
import ru.anfdenis.user.Client
import scala.concurrent.Await
import akka.actor.{Props, ActorSystem}
import spray.http.MediaTypes._

/**
 * Denis Anfertev
 * 23.05.13 17:14
 */
object Run extends App with SimpleRoutingApp {
  val actorSystem = ActorSystem("simple-system")

  val actorService = actorSystem.actorOf(Props[SimpleGetActor])

  startServer(interface = "localhost", port = 8080) {
    path("user" / IntNumber) {
      (id: Int) => {
        get {
          complete {
            implicit val timeout = Timeout(5 seconds)
            val future = (actorService ? id)
            try {
              Await.result(future, timeout.duration) match {
                case akka.actor.Status.Failure(e) => throw e
                case c: Client => c
              }
            } catch {
              case e: Exception => e.getMessage
            }
          }
        }
      }
    } ~
      path("user/post") {
        post {
          entity(as[Client]) {
            (client: Client) =>
              complete(client)
          }
        }
      }
  }
}
