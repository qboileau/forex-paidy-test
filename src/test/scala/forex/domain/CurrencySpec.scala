package forex.domain

import forex.processes.rates.messages.ProcessError
import org.scalatest.{Matchers, WordSpec}

class CurrencySpec  extends WordSpec with Matchers {

  "Currency" should {

    "parse valid string" in {
      Currency.fromString("EUR") should ===(Currency.EUR)
      Currency.fromString("jpy") should ===(Currency.JPY)
    }

    "rise an error if unknown string" in {
      an [ProcessError.Parsing] should be thrownBy Currency.fromString("NOT")
    }
  }
}
