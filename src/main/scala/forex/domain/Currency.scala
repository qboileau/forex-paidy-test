package forex.domain

import cats.syntax.either._
import cats.Show
import forex.processes.rates.messages.ProcessError
import io.circe._

sealed trait Currency
object Currency {
  final case object AUD extends Currency
  final case object CAD extends Currency
  final case object CHF extends Currency
  final case object EUR extends Currency
  final case object GBP extends Currency
  final case object NZD extends Currency
  final case object JPY extends Currency
  final case object SGD extends Currency
  final case object USD extends Currency

  def all: Seq[Currency] = Seq(AUD, CAD, CHF, EUR, GBP, NZD, JPY, SGD, USD)

  implicit val currencyShow: Show[Currency] = Show.show {
    case AUD ⇒ "AUD"
    case CAD ⇒ "CAD"
    case CHF ⇒ "CHF"
    case EUR ⇒ "EUR"
    case GBP ⇒ "GBP"
    case NZD ⇒ "NZD"
    case JPY ⇒ "JPY"
    case SGD ⇒ "SGD"
    case USD ⇒ "USD"
  }

  def fromString(s: String): Currency = s match {
    case "AUD" | "aud" ⇒ AUD
    case "CAD" | "cad" ⇒ CAD
    case "CHF" | "chf" ⇒ CHF
    case "EUR" | "eur" ⇒ EUR
    case "GBP" | "gbp" ⇒ GBP
    case "NZD" | "nzd" ⇒ NZD
    case "JPY" | "jpy" ⇒ JPY
    case "SGD" | "sgd" ⇒ SGD
    case "USD" | "usd" ⇒ USD
    case _             ⇒ throw ProcessError.Parsing(s"Unknown currency code : $s")
  }

  implicit val encoder: Encoder[Currency] = Encoder.encodeString.contramap(currencyShow.show)

  implicit val decoder: Decoder[Currency] = Decoder.decodeString.emap[Currency] { str ⇒
    Either.catchNonFatal(Currency.fromString(str)).leftMap(t ⇒ t.getMessage)
  }
}
