package forex.services.oneforge

import cats.Eval
import cats.effect._
import cats.implicits._
import com.typesafe.scalalogging.LazyLogging
import forex.config.{ApplicationConfig, OneForgeConfig}
import forex.domain.Rate
import forex.services.oneforge.Converters.{toPairSymbol, toRate}
import forex.services.oneforge.protocol.{PairSymbol, Quote}
import org.http4s.Uri
import org.http4s.circe.jsonOf
import org.http4s.client.blaze._
import org.zalando.grafter._
import org.zalando.grafter.macros.readerOf

import scala.concurrent.duration._

@readerOf[ApplicationConfig]
case class OneForgeClient(
  config: OneForgeConfig
) extends Start
  with Stop
  with LazyLogging {

  private val clientConfig = BlazeClientConfig.defaultConfig.copy(
    requestTimeout = 10.second
  )

  private lazy val httpClient = Http1Client[IO](clientConfig).unsafeRunSync

  implicit val quotesDecoder = jsonOf[IO, List[Quote]]

  override def start: Eval[StartResult] = StartResult.eval("OneForgeHttpClient") {
    if (config.apiKey.isEmpty) StartFailure("OneForge api key not provided")
    else {
      httpClient
      StartOk
    }
  }

  override def stop: Eval[StopResult] = StopResult.eval("OneForgeHttpClient") {
    httpClient.shutdownNow()
  }

  def getAllRates(): IO[Either[ServiceError, List[Rate]]] = {
    val uri = quoteUrl(Rate.Pair.allSupported.map(toPairSymbol))
    logger.debug(s"URI: $uri")
    httpClient
      .expect[List[Quote]](uri)
      .map(_.map(toRate).sequence)
  }


  def getRate(pair: Rate.Pair*): IO[Either[ServiceError, Rate]] = {
    val uri = quoteUrl(pair.map(toPairSymbol))
    logger.debug(s"URI: $uri")
    httpClient
      .expect[List[Quote]](uri)
      .map(_.head)
      .map(toRate)
  }

  private def quoteUrl(symbols: Seq[PairSymbol]): Uri = {
    import cats.implicits._

    val pairParam = symbols.map(_.show).mkString(",")
    Uri.unsafeFromString(s"${config.baseUrl}/quotes?pairs=$pairParam&api_key=${config.apiKey}")
  }
}
