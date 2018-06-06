package forex.services.oneforge

import java.time.OffsetDateTime

import akka.actor.ActorSystem
import akka.http.scaladsl.unmarshalling
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, Unmarshal, Unmarshaller}
import akka.stream.Materializer
import forex.config.OneForgeConfig
import forex.domain._
import forex.interfaces.api.utils.ApiMarshallers
import forex.services.oneforge.ServiceError.Generic
import forex.services.oneforge.protocol.{PairSymbol, Quotes}
import monix.eval.Task
import org.atnos.eff._
import org.atnos.eff.addon.monix.task._

import scala.concurrent.{Await, ExecutionContext}

object Interpreters {
  def dummy[R](
    implicit
    m1: _task[R]
  ): Algebra[Eff[R, ?]] = new Dummy[R]

  def live[R](
    oneForgeConfig: OneForgeConfig
  )(
    implicit
    m1: _task[R],
    actorSystem: ActorSystem,
    materializer: Materializer,
    ec: ExecutionContext
  ): Algebra[Eff[R, ?]] = new Live[R](oneForgeConfig)
}

private[oneforge] final class Dummy[R](
  implicit
  m1: _task[R]
) extends Algebra[Eff[R, ?]] {
  override def get(
    pair: Rate.Pair
  ): Eff[R, ServiceError Either Rate] =
    for {
      result ← fromTask(Task.now(Rate(pair, Price(BigDecimal(100)), Timestamp.now)))
    } yield Right(result)
}

private[oneforge] final class Live[R](
  oneForgeConfig: OneForgeConfig
)(
  implicit
  m1: _task[R],
  actorSystem: ActorSystem,
  materializer: Materializer,
  ec: ExecutionContext
) extends Algebra[Eff[R, ?]] {

  import ApiMarshallers._
  import forex.services.oneforge.Converters._
  import akka.http.scaladsl.Http
  import akka.http.scaladsl.model._
  import scala.concurrent.duration._

  override def get(
    pair: Rate.Pair
  ): Eff[R, ServiceError Either Rate] =
    fromTask(getQuote(pair))

  private def getQuote(pair: Rate.Pair): Task[Either[ServiceError, Rate]] = {
    Task.fromFuture {
      val uri = quoteUrl(toPairSymbol(pair))
      println(s"URI: $uri")
      Http().singleRequest(HttpRequest(uri = uri)).flatMap { response =>
        println(s"response : ${Await.result(response.entity.toStrict(500.millis), 500.millis)}")
        Unmarshal(response.entity).to[List[Quotes]].map(_.head).map(toRate)
      }
    }
  }

  private def quoteUrl(symbol: PairSymbol): Uri = {
    import cats.implicits._
    s"${oneForgeConfig.baseUrl}/quotes?pairs=${symbol.show}&api_key=${oneForgeConfig.apiKey}"
  }
}