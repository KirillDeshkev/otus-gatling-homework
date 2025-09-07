package webtours

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder

import scala.util.Random

object Actions {

  val getSessionCookie: HttpRequestBuilder = http("getSessionCookie")
    .get("/cgi-bin/welcome.pl")
    .queryParam("signOff", "true")
    .check(status is 200)

  val getStartPage: HttpRequestBuilder = http("getStartPage")
    .get("/cgi-bin/nav.pl")
    .check(regex("""userSession"\s+value="([^"]+)"""").saveAs("userSession"))
    .check(status is 200)

  val login: HttpRequestBuilder = http("login")
    .post("/cgi-bin/login.pl")
    .formParam("userSession", "#userSession")
    .formParam("username", "#username")
    .formParam("password", "#password")
    .formParam("login.x", "0")
    .formParam("login.y", "0")
    .formParam("JSFormSubmit", "off")
    .check(status is 200)

  val getDirections: HttpRequestBuilder = http("getDirections")
    .get("/cgi-bin/reservations.pl")
    .queryParam("page", "welcome")
    .check(css("select[name=depart] option", "value").findAll.saveAs("departCities"))
    .check(css("select[name=arrive] option", "value").findAll.saveAs("arriveCities"))
    .check(css("input[name=departDate]", "value").saveAs("departDate"))
    .check(css("input[name=returnDate]", "value").saveAs("returnDate"))
    .check(status is 200)

  val pickRandomCities: ChainBuilder = exec { session =>
    val depCities = session("departCities").as[Vector[String]]
    val arrCities = session("arriveCities").as[Vector[String]]

    val dep = depCities(Random.nextInt(depCities.size))
    val arr = arrCities(Random.nextInt(arrCities.size))

    session
      .set("departCity", dep)
      .set("arriveCity", arr)
  }

  val getFlights: HttpRequestBuilder = http("getFlights")
    .post("/cgi-bin/reservations.pl")
    .formParam("advanceDiscount", "0")
    .formParam("depart", "#{departCity}")
    .formParam("departDate", "#{departDate}")
    .formParam("arrive", "#{arriveCity}")
    .formParam("returnDate", "#{returnDate}")
    .formParam("numPassengers", "1")
    .formParam("seatPref", "None")
    .formParam("seatType", "Coach")
    .formParam("findFlights.x", "76")
    .formParam("findFlights.y", "3")
    .formParam(".cgifields", "roundtrip")
    .formParam(".cgifields", "seatType")
    .formParam(".cgifields", "seatPref")
    .check(css("input[name=outboundFlight]", "value").findAll.saveAs("outboundFlights"))
    .check(status is 200)

  val pickFlight: ChainBuilder = exec { session =>
    val flights = session("outboundFlights").as[Vector[String]]
    val randomFlight = flights(scala.util.Random.nextInt(flights.size))
    session.set("outboundFlight", randomFlight)
  }

  val selectFlight: HttpRequestBuilder = http("selectFlight")
    .post("/cgi-bin/reservations.pl")
    .formParam("outboundFlight", "#{outboundFlight}")
    .formParam("numPassengers", "1")
    .formParam("advanceDiscount", "0")
    .formParam("seatType", "Coach")
    .formParam("seatPref", "None")
    .formParam("reserveFlights.x", "65")
    .formParam("reserveFlights.y", "14")
    .check(status is 200)

  val purchase: HttpRequestBuilder = http("purchase")
    .post("/cgi-bin/reservations.pl")
    .formParam("firstName", "John")
    .formParam("lastName", "Doe")
    .formParam("address1", "1st street 15")
    .formParam("address2", "12312")
    .formParam("pass1", "John Doe")
    .formParam("creditCard", "#{cardNumber}")
    .formParam("expDate", "#{cardExp}")
    .formParam("saveCC", "on")
    .formParam("oldCCOption", "")
    .formParam("numPassengers", "1")
    .formParam("seatType", "Coach")
    .formParam("seatPref", "None")
    .formParam("outboundFlight", "#{outboundFlight}")
    .formParam("advanceDiscount", "0")
    .formParam("returnFlight", "")
    .formParam("JSFormSubmit", "off")
    .formParam("buyFlights.x", "68")
    .formParam("buyFlights.y", "7")
    .formParam(".cgifields", "saveCC")
    .check(status is 200)

  val goBack: HttpRequestBuilder = http("goBack")
    .post("/cgi-bin/reservations.pl")
    .formParam("Book Another.x", "42")
    .formParam("Book Another.y", "10")
    .check(status is 200)
}
