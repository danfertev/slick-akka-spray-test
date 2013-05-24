package ru.anfdenis.user

import spray.httpx.marshalling.{MarshallingContext, Marshaller}
import spray.http.MediaTypes._
import spray.http.{HttpBody, ContentType}
import spray.httpx.unmarshalling.Unmarshaller

import spray.json._
import ru.anfdenis.json.TestJsonProtocol._
import java.io.ByteArrayInputStream


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

  override def equals(obj: Any): Boolean = obj match {
    case admin: Admin => admin.id == id && admin.name == name && admin.adminOnly == adminOnly
    case _ => false
  }
}

object Admin {
  implicit val adminMarshaller =
    Marshaller.of[Admin](`application/xml`, `application/json`) {
      (admin: Admin, contentType: ContentType, ctx: MarshallingContext) => contentType match {
        case ContentType(`application/xml`, _) => ctx.marshalTo(HttpBody(contentType, admin.toXml.toString()))
        case ContentType(`application/json`, _) => ctx.marshalTo(HttpBody(contentType, admin.toJson.toString()))
      }
    }

  implicit val adminUnmarshaller =
    Unmarshaller[Admin](`application/xml`, `application/json`) {
      case HttpBody(contentType, buffer) => contentType match {
        case ContentType(`application/xml`, _) =>  fromXml(xml.XML.load(new ByteArrayInputStream(buffer)))
        case ContentType(`application/json`, _) => buffer.mkString.asJson.convertTo[Admin]
      }
    }

  def fromXml(node: xml.NodeSeq): Admin = Admin(
    Some((node \ "@id").text.toInt),
    (node \ "name").text,
    (node \ "adminOnly").text
  )


  def apply(id: Option[Int], name: String, adminOnly: String): Admin = new Admin(id, name, adminOnly)

  def apply(id: Int, name: String, adminOnly: String): Admin = new Admin(Some(id), name, adminOnly)
}