package forex.services.oneforge.protocol

import forex.domain._
import io.circe._
import io.circe.generic.semiauto._

case class Quote(
  symbol: PairSymbol,
  price: Price,
  timestamp: QuoteTime
)

object Quote {
  implicit val decoder: Decoder[Quote] = deriveDecoder[Quote]
}

