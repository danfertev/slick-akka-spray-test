package ru.anfdenis.user

import spray.httpx.marshalling._
import spray.httpx.unmarshalling._
import spray.http.MediaTypes._
import spray.http.{ContentType, HttpBody}
import java.io.ByteArrayInputStream

import spray.json._
import ru.anfdenis.json.TestJsonProtocol._

/**
 * Denis Anfertev
 * 23.05.13 12:42
 */
class Client(id: Option[Int], name: String, val clientOnly: String) extends User(id, name) {
  def isAdmin: Boolean = false

  var admin = Admin(None, "admin", "adminOnly")

  def toXml: xml.Node =
    <client id={id.getOrElse(-1).toString}>
      <name>{name}</name>
      <clientOnly>{clientOnly}</clientOnly>
      {admin.toXml}
    </client>

  override def equals(obj: Any): Boolean = obj match {
    case c: Client => c.id == id && c.name == name && c.clientOnly == clientOnly && c.admin == admin
    case _ => false
  }
}

object Client {

  implicit val clientMarshaller =
    Marshaller.of[Client](`application/xml`, `application/json`) {
      (client: Client, contentType: ContentType, ctx: MarshallingContext) => contentType match {
        case ContentType(`application/xml`, _) => ctx.marshalTo(HttpBody(contentType, client.toXml.toString()))
        case ContentType(`application/json`, _) => ctx.marshalTo(HttpBody(contentType, client.toJson.toString()))
      }
    }

  implicit val clientUnmarshaller =
    Unmarshaller[Client](`application/xml`, `application/json`) {
      case HttpBody(contentType, buffer) => contentType match {
        case ContentType(`application/xml`, _) => fromXml(xml.XML.load(new ByteArrayInputStream(buffer)))
        case ContentType(`application/json`, _) => buffer.mkString.asJson.convertTo[Client]
      }


    }

  def fromXml(node: xml.NodeSeq): Client = {
    val c = Client(
      Some((node \ "@id").text.toInt),
      (node \ "name").text,
      (node \ "clientOnly").text
    )
    c.admin = Admin.fromXml(node \ "admin")
    c
  }

  def apply(id: Option[Int], name: String, clientOnly: String): Client = new Client(id, name, clientOnly)

  def apply(id: Int, name: String, clientOnly: String): Client = new Client(Some(id), name, clientOnly)
}
