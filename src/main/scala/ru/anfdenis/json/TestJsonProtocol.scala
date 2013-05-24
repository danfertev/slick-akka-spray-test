package ru.anfdenis.json

import spray.json._
import ru.anfdenis.user._

/**
 * Denis Anfertev
 * 24.05.13 17:15
 */
object TestJsonProtocol extends DefaultJsonProtocol {

  implicit object AdminJsonFormat extends RootJsonFormat[Admin] {
    def write(a: Admin) =
      JsObject(
        "id" -> JsNumber(a.id.getOrElse(-1)),
        "name" -> JsString(a.name),
        "adminOnly" -> JsString(a.adminOnly)
      )

    def read(value: JsValue) = {
      value.asJsObject.getFields("id", "name", "adminOnly") match {
        case Seq(JsNumber(id), JsString(name), JsString(adminOnly)) =>
          Admin(id.toInt, name, adminOnly)
        case _ => throw new DeserializationException("Client expected")
      }
    }
  }

  implicit object ClientJsonFormat extends RootJsonFormat[Client] {
    def write(c: Client) =
      JsObject(
        "id" -> JsNumber(c.id.getOrElse(-1)),
        "name" -> JsString(c.name),
        "clientOnly" -> JsString(c.clientOnly),
        "admin" -> c.admin.toJson
      )

    def read(value: JsValue) = {
      value.asJsObject.getFields("id", "name", "clientOnly", "admin") match {
        case Seq(JsNumber(id), JsString(name), JsString(clientOnly), admin) =>{
          val c = Client(Some(id.toInt), name, clientOnly)
          c.admin = admin.convertTo[Admin]
          c
        }
        case Seq(JsNumber(id), JsString(name), JsString(clientOnly)) =>
          Client(Some(id.toInt), name, clientOnly)
        case _ => throw new DeserializationException("Client expected")
      }
    }
  }

}
