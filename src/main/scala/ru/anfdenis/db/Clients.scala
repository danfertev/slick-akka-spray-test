package ru.anfdenis.db

import scala.slick.driver.H2Driver.simple._

import ru.anfdenis.user._

/**
 * Denis Anfertev
 * 23.05.13 12:48
 */
object Clients extends Table[Client]("CLIENTS") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def clientOnly = column[String]("clientOnly")

  def * = id.? ~ name ~ clientOnly <> (t => Client(t._1, t._2, t._3), (c: Client) => Some(c.id, c.name, c.clientOnly))

  def ** = name ~ clientOnly <> (tc => Client(None, tc._1, tc._2), (c: Client) => Some(c.name, c.clientOnly))
}

