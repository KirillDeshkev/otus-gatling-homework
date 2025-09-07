import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

package object webtours {

  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl("http://webtours.load-test.ru:1080")
    .disableFollowRedirect

}
