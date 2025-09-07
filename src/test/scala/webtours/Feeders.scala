package webtours

import io.gatling.core.Predef._
import io.gatling.core.feeder.BatchableFeederBuilder
import io.gatling.http.Predef._

object Feeders {

  val credentials: BatchableFeederBuilder[String] = csv("credentials.csv").random

  val cards: BatchableFeederBuilder[String] = csv("cards.csv").random

}
