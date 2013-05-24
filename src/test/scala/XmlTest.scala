import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import ru.anfdenis.user.{Admin, Client}

/**
 * Denis Anfertev
 * 24.05.13 16:30
 */
@RunWith(classOf[JUnitRunner])
class XmlTest extends FunSuite {
  test("client to xml") {
    Client(Some(1), "test", "test1").toXml ===
      <client id="1">
        <name>test</name>
        <clientOnly>test1</clientOnly>
        <admin id ="-1">
          <name>admin</name>
          <adminOnly>adminOnly</adminOnly>
        </admin>
      </client>
  }

  test("xml to client") {
    val xml =
    "<client id=\"1\">" +
      "<name>test</name>" +
      "<clientOnly>test1</clientOnly>" +
      "<admin id =\"2\">" +
        "<name>test2</name>" +
        "<adminOnly>test3</adminOnly>" +
      "</admin>" +
    "</client>"
    Client.fromXml(scala.xml.XML.loadString(xml)) === {
      val c = Client(Some(1), "test", "test1")
      c.admin = Admin(Some(2),"test2", "test3")
      c
    }
  }
}
