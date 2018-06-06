package forex.services.oneforge

import java.time.OffsetDateTime

import forex.config.OneForgeConfig
import forex.domain._
import forex.main.ActorSystems
import monix.eval.Task
import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.addon.monix.task._
import org.zalando.grafter.macros.reader

object Interpreters {
  def dummy[R](
      implicit
      m1: _task[R]
  ): Algebra[Eff[R, ?]] = new Dummy[R]


  def live[R](
    oneForgeConfig: OneForgeConfig,
    actorSystems: ActorSystems
  )(
    implicit
    m1: _task[R]
  ): Algebra[Eff[R, ?]] = new Live[R](oneForgeConfig, actorSystems)
}

private[oneforge] final class Dummy[R] (
    implicit
    m1: _task[R]
) extends Algebra[Eff[R, ?]] {
  override def get(
      pair: Rate.Pair
  ): Eff[R, Error Either Rate] =
    for {
      result ← fromTask(Task.now(Rate(pair, Price(BigDecimal(100)), Timestamp.now)))
    } yield Right(result)
}

private[oneforge] final class Live[R] (
  oneForgeConfig: OneForgeConfig,
  actorSystems: ActorSystems
)(
  implicit
  m1: _task[R]
) extends Algebra[Eff[R, ?]] {

  import akka.http.scaladsl.Http
  import akka.http.scaladsl.model._

  override def get(
    pair: Rate.Pair
  ): Eff[R, Error Either Rate] =
    for {
      result ← fromTask(getQuote(pair))
    } yield Right(result)

  private def getQuote(pair: Rate.Pair): Task[Rate] = {
    Task.now(Rate(pair, Price(BigDecimal(100)), Timestamp.now))
  }

  private def quoteUrl(pair: Rate.Pair): Uri = {
    import cats.implicits._
    s"${oneForgeConfig.baseUrl}/quotes?paires=${pair.show}&api_key=${oneForgeConfig.apiKey}"
  }

}