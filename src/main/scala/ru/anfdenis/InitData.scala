package ru.anfdenis

import scala.slick.driver.PostgresDriver.simple._

import Database.threadLocalSession
import ru.anfdenis.db._
import ru.anfdenis.user._
import scala.slick.jdbc.meta.MTable

/**
 * Denis Anfertev
 * 23.05.13 13:13
 */
object InitData extends App {
  val db = Database.forURL("jdbc:postgresql://192.168.1.55:5432/users?user=postgres&password=RAPtor1234!", driver = "org.postgresql.Driver")
  val ddl = (Clients.ddl ++ Admins.ddl)

  db withSession {
    if (MTable.getTables("CLIENTS").elements.isEmpty) ddl.create

    Clients.** insert Client(None, "client1", "client1")
    Clients.** insert Client(None, "client2", "client2")
    Clients.** insert Client(None, "client3", "client3")
    Admins.** insert Admin(None, "admin1", "admin1")
    Admins.** insert Admin(None, "admin2", "admin2")
  }
}
