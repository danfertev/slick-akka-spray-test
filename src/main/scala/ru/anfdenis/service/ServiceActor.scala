package ru.anfdenis.service

import akka.pattern.ask
import ru.anfdenis.actor._
import akka.util.Timeout
import scala.concurrent.duration._
import ru.anfdenis.user.Client
import scala.concurrent.Await
import akka.actor.{Actor, Props, ActorSystem}
import ru.anfdenis.InitData
import spray.routing._
import spray.routing.authentication.BasicAuth

/**
 * Denis Anfertev
 * 23.05.13 17:14
 */
class ServiceActor extends Actor with HttpServiceActor {
  InitData.main(Array())

  val actorSystem = ActorSystem("simple-system")

  val actorService = actorSystem.actorOf(Props[SimpleGetActor])


  def receive = runRoute(route)

  def route =
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
      } ~
      path("login") {
        get {
          authenticate(BasicAuth()) {
            user =>
              complete("Authorized!")
          }
        }
      }
}
