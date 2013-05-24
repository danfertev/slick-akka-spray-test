package ru.anfdenis.user


/**
 * Denis Anfertev
 * 23.05.13 12:40
 */
abstract class User(val id: Option[Int], val name: String) {
  def isAdmin: Boolean

  override def toString: String = this.getClass.getSimpleName + "[" + id.getOrElse(-1) + "]"
}


