package ru.anfdenis

import scala.slick.driver.H2Driver.simple._

import Database.threadLocalSession
import ru.anfdenis.db._
import ru.anfdenis.user._
import scala.slick.jdbc.meta.MTable

/**
 * Denis Anfertev
 * 23.05.13 13:13
 */
object InitData{

  val db = Database.forURL("jdbc:h2:mem:test1;DB_CLOSE_DELAY=-1 ", driver = "org.h2.Driver")
  val ddl = (Clients.ddl ++ Admins.ddl)

  def main(args:Array[String]) {
    db withSession {
      if (MTable.getTables("CLIENTS").elements.isEmpty) ddl.create

      Clients.** insert Client(None, "client1", "client1")
      Clients.** insert Client(None, "client2", "client2")
      Clients.** insert Client(None, "client3", "client3")
      Admins.** insert Admin(None, "admin1", "admin1")
      Admins.** insert Admin(None, "admin2", "admin2")
    }
  }
}
