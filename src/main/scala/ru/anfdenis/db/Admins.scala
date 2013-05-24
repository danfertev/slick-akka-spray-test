package ru.anfdenis.db

import scala.slick.driver.PostgresDriver.simple._

import ru.anfdenis.user._

/**
 * Denis Anfertev
 * 23.05.13 12:48
 */
object Admins extends Table[Admin]("ADMINS") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def adminOnly = column[String]("adminOnly")

  def * = id.? ~ name ~ adminOnly <> (t => Admin(t._1, t._2, t._3), (a: Admin) => Some(a.id, a.name, a.adminOnly))

  def ** = name ~ adminOnly <> (ta => Admin(None, ta._1, ta._2), (a: Admin) => Some(a.name, a.adminOnly))
}
