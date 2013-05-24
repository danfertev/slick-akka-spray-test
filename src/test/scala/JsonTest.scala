import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import ru.anfdenis.user.{Admin, Client}

import spray.json._
import ru.anfdenis.json.TestJsonProtocol._

/**
 * Denis Anfertev
 * 24.05.13 17:31
 */
@RunWith(classOf[JUnitRunner])
class JsonTest extends FunSuite {
  test("client to json") {
    Client(1, "test", "test1").toJson ===
      """{"id":1,"name":"test","clientOnly":"test1",
        | "admin":{"id":-1,"name":"admin","adminOnly":"adminOnly"}}""".stripMargin.asJson
  }

  test("json to client") {
    """{"id":1,"name":"test","clientOnly":"test1"}""".asJson.convertTo[Client] === Client(1, "test", "test1")
  }

  test("json to client with nested object") {
    """{"id":1,"name":"test","clientOnly":"test1",
      |"admin":{"id":2,"name":"nested","adminOnly":"nested"}}""".stripMargin.asJson.convertTo[Client] === {
      val c = Client(1, "test", "test1")
      c.admin = Admin(2,"nested","nested")
      c
    }
  }
}
