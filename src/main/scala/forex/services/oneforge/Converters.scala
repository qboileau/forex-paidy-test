package forex.services.oneforge

import java.time.ZoneOffset

import cats.implicits._
import forex.domain.{ Currency, Rate, Timestamp ⇒ DTimestamp }
import forex.services.oneforge.ServiceError.Generic
import forex.services.oneforge.protocol.{ PairSymbol, Quote, QuoteTime ⇒ PTimestamp }

/**
  * Converter OnForge protocol => Domain object
  */
object Converters {

  def toRate(
      quote: Quote
  ): ServiceError Either Rate =
    toPair(quote.symbol).map { currencyPair ⇒
      Rate(
        pair = currencyPair,
        price = quote.price,
        timestamp = toTimestamp(quote.timestamp)
      )
    }

  def toPair(
      symbol: PairSymbol
  ): Either[ServiceError, Rate.Pair] =
    symbol.value.grouped(3).map(Currency.fromString).take(2).toList match {
      case from :: to :: Nil ⇒ Right(Rate.Pair(from, to))
      case _                 ⇒ Left(Generic(s"Unable to parse symbol ${symbol.value} into currency pair"))
    }

  def toTimestamp(
      timestamp: PTimestamp
  ): DTimestamp =
    DTimestamp(timestamp.time.atOffset(ZoneOffset.UTC))

  def toPairSymbol(
      pair: Rate.Pair
  ): PairSymbol =
    PairSymbol(pair.show)
}
