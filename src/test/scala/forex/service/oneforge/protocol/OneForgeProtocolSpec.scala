package forex.service.oneforge.protocol

import java.time.{Instant, ZoneOffset}

import forex.domain.{Currency, Price, Rate, Timestamp}
import forex.processes.rates.messages.ProcessError
import forex.services.oneforge.protocol._
import forex.services.oneforge.Converters._
import io.circe.parser.decode
import org.scalatest._

import scala.language.postfixOps

class OneForgeProtocolSpec extends WordSpec with Matchers {

  "Quotes" should {

    "be decoded successfully from JSON" in {
      val json =
        """
          |{
          |  "symbol": "EURUSD",
          |  "bid": 1.17998,
          |  "ask": 1.17999,
          |  "price": 1.17999,
          |  "timestamp": "1528402819"
          |}
        """.stripMargin

      val expected = Quote(
        PairSymbol("EURUSD"),
        Price(1.17999),
        QuoteTime(Instant.ofEpochSecond(1528402819l))
      )

      decode[Quote](json).right.get should ===(expected)
    }

    "can be converted in Rate" in {
      val quote = Quote(
        PairSymbol("EURUSD"),
        Price(1.17999),
        QuoteTime(Instant.ofEpochSecond(1528402819l))
      )

      val expected = Rate(
        Rate.Pair(Currency.EUR, Currency.USD),
        Price(1.17999),
        Timestamp(Instant.ofEpochSecond(1528402819l).atOffset(ZoneOffset.UTC))
      )

      toRate(quote).right.get should ===(expected)
    }


  }

  "PairSymbol" should {

    "convert to currency Pair" in {
      val symbol = PairSymbol("EURJPY")
      toPair(symbol) should ===(Right(Rate.Pair(Currency.EUR, Currency.JPY)))
    }

    "fail to convert with invalid currency in PairSymbol" in {
      val symbol = PairSymbol("EURUNK")
      an [ProcessError.Parsing] should be thrownBy toPair(symbol)
    }

    "fail to convert PairSymbol is empty" in {
      val symbol = PairSymbol("")
      toPair(symbol) should be leftSide
    }

    "fail to convert PairSymbol contain one currency" in {
      val symbol = PairSymbol("EUR")
      toPair(symbol) should be leftSide
    }

    "fail to convert PairSymbol contain more than two currencies" in {
      val symbol = PairSymbol("USDEURJPY")
      toPair(symbol) should be leftSide
    }
  }
}
