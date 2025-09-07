package webtours

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class DebugScenario extends Simulation {
  setUp(Scenario().inject(atOnceUsers(1)))
    .protocols(httpProtocol)
    .maxDuration(1000)
}
