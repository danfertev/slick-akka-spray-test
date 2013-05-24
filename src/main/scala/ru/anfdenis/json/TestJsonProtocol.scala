package ru.anfdenis.json

import spray.json._
import ru.anfdenis.user._

/**
 * Denis Anfertev
 * 24.05.13 17:15
 */
object TestJsonProtocol extends DefaultJsonProtocol {

  implicit object ClientJsonFormat extends RootJsonFormat[Client] {
    def write(c: Client) =
      JsObject(
        "id" -> JsNumber(c.id.getOrElse(-1)),
        "name" -> JsString(c.name),
        "clientOnly" -> JsString(c.clientOnly)
      )

    def read(value: JsValue) = {
      value.asJsObject.getFields("id", "name", "clientOnly") match {
        case Seq(JsNumber(id), JsString(name), JsString(clientOnly)) =>
          Client(Some(id.toInt), name, clientOnly)
        case _ => throw new DeserializationException("Color expected")
      }
    }
  }

}
