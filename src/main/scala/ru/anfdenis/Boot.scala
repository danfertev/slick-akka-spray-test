package ru.anfdenis

import akka.actor.{Props, ActorSystem}
import spray.io.{SingletonHandler, IOExtension}
import ru.anfdenis.service.ServiceActor
import spray.can.server.HttpServer

/**
 * Denis Anfertev
 * 30.05.13 15:56
 */
object Boot extends App {
  val system = ActorSystem("s-a-s-test")

  private val ioBridge = IOExtension(system).ioBridge()

  val service = system.actorOf(Props[ServiceActor], "service")

  val httpServer = system.actorOf(
    Props(new HttpServer(ioBridge, SingletonHandler(service))),
    name = "http-server"
  )
  httpServer ! HttpServer.Bind("127.0.0.1", 8080)
}
