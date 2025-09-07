package webtours

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class Stability extends Simulation {
  setUp(Scenario().inject(rampConcurrentUsers(0).to(145).during(5.minutes),
    constantConcurrentUsers(145).during(1.hour))).protocols(httpProtocol)
    .assertions(
      global.successfulRequests.percent.gt(95)
    )
}
