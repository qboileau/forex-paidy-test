package forex.services.oneforge.protocol

import forex.domain._
import io.circe._
import io.circe.generic.semiauto._

case class Quotes(
  symbol: PairSymbol,
  price: Price,
  timestamp: QuoteTime
)

object Quotes {

  implicit val decoder: Decoder[Quotes] = deriveDecoder[Quotes]
}

