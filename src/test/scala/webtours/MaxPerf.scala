package webtours

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class MaxPerf extends Simulation {
//  setUp(Scenario().inject(atOnceUsers(1),
//    rampUsers(50).during(10.minutes)))
//    .protocols(httpProtocol)
//    .assertions(
//      global.successfulRequests.percent.gt(95)
//    )

//  setUp(Scenario().inject(incrementUsersPerSec(1)
//    .times(5)
//    .eachLevelLasting(1.minutes)
//    .separatedByRampsLasting(10.seconds)
//    .startingFrom(1)))
//    .protocols(httpProtocol)
//    .assertions(
//      global.successfulRequests.percent.gt(95)
//    )

  setUp(Scenario().inject(incrementConcurrentUsers(18)
    .times(10)
    .eachLevelLasting(2.minutes)
    .separatedByRampsLasting(30.seconds)
    .startingFrom(0)))
    .protocols(httpProtocol)
    .assertions(
      global.successfulRequests.percent.gt(95)
    )
}
