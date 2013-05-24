import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import ru.anfdenis.user.Client

import spray.json._
import ru.anfdenis.json.TestJsonProtocol._

/**
 * Denis Anfertev
 * 24.05.13 17:31
 */
@RunWith(classOf[JUnitRunner])
class JsonTest extends FunSuite {
  test("client to json") {
    Client(1, "test", "test1").toJson === """{"id":1,"name":"test","clientOnly":"test1"}""".asJson
  }
}
