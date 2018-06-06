package forex.services.oneforge.protocol

import cats.Show
import io.circe.Decoder
import io.circe.generic.extras.wrapped.deriveUnwrappedDecoder

case class PairSymbol(value: String) extends AnyVal

object PairSymbol {

  implicit val show: Show[PairSymbol] = Show.show[PairSymbol](_.value)

  implicit val decoder: Decoder[PairSymbol] = deriveUnwrappedDecoder[PairSymbol]
}