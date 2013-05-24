import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import ru.anfdenis.db._
import ru.anfdenis.InitData
import ru.anfdenis.user.User

/**
 * Denis Anfertev
 * 23.05.13 13:49
 */
@RunWith(classOf[JUnitRunner])
class DbInitDataTest extends FunSuite {
  test("init data test") {
    import scala.slick.driver.H2Driver.simple._
    import Database.threadLocalSession

    InitData.main(Array())

    InitData.db withSession {
      val allClient = (for (c <- Clients) yield c).list
      val allAdmin = (for (a <- Admins) yield a).list
      val all: List[User] = allClient ++ allAdmin

      all.length === 5
    }
  }
}
