package webtours

import io.gatling.core.Predef._
import io.gatling.core.structure._
import webtours.Feeders._

object Scenario {
  def apply(): ScenarioBuilder = new Scenario().scn
}

class Scenario {

  val planJourneyGroup: ChainBuilder = group("planJourneyGroup") {
    exec(Actions.getDirections)
      .exec(Actions.pickRandomCities)
  }

  val chooseFlightGroup: ChainBuilder = group("chooseFlightGroup") {
    exec(Actions.getFlights)
      .exec(Actions.pickFlight)
  }

  val scn: ScenarioBuilder = scenario("Flight book")
    .feed(credentials)
    .feed(cards)
    .exec(Actions.getSessionCookie)
    .exec(Actions.getStartPage)
    .exec(Actions.login)
    .exec(planJourneyGroup)
    .exec(chooseFlightGroup)
    .exec(Actions.selectFlight)
    .exec(Actions.purchase)
    .exec(Actions.goBack)
}
