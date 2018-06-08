package forex.services.oneforge.protocol

import java.time.Instant

import cats.syntax.either._
import io.circe.Decoder
import io.circe.generic.extras.semiauto.deriveUnwrappedDecoder

case class QuoteTime(time: Instant) extends AnyVal

object QuoteTime {

  implicit val instantDecoder: Decoder[Instant] = Decoder.decodeLong.emap[Instant] { seconds =>
    Either.right(Instant.ofEpochSecond(seconds))
  }
  implicit val decoder: Decoder[QuoteTime] = deriveUnwrappedDecoder[QuoteTime]

}