package ru.anfdenis.user

import spray.httpx.marshalling.Marshaller
import spray.http.MediaTypes._
import spray.http.HttpBody
import spray.httpx.unmarshalling.Unmarshaller


/**
 * Denis Anfertev
 * 23.05.13 12:44
 */
class Admin(id: Option[Int], name: String, val adminOnly: String) extends User(id, name) {
  def isAdmin: Boolean = true

  def toXml: xml.Node =
    <admin id={id.getOrElse(-1).toString}>
      <name>{name}</name>
      <adminOnly>{adminOnly}</adminOnly>
    </admin>
}

object Admin {
  implicit val adminToXML =
    Marshaller.of[Admin](`application/xml`) {
      (admin, contentType, ctx) => ctx.marshalTo(HttpBody(contentType, admin.toXml.toString()))
    }

  implicit val xmlToAdmin =
    Unmarshaller[Admin](`application/xml`) {
      case HttpBody(contentType, buffer) => fromXml(xml.XML.load(buffer.mkString))
    }

  def fromXml(node: xml.NodeSeq): Admin = Admin(
    Some((node \ "@id").text.toInt),
    (node \ "name").text,
    (node \ "adminOnly").text
  )


  def apply(id: Option[Int], name: String, adminOnly: String): Admin = new Admin(id, name, adminOnly)
}