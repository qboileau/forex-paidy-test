package forex.services.oneforge

import cats.syntax.show._
import forex.domain._
import forex.services.oneforge.ServiceError.Generic
import monix.eval.Task
import org.atnos.eff._
import org.atnos.eff.addon.monix.task._
import org.atnos.eff.memo._memo

object Interpreters {
  def dummy[R](
      implicit
      m1: _task[R]
  ): Algebra[Eff[R, ?]] = new Dummy[R]

  def live[R](
      oneForgeClient: OneForgeClient
  )(
      implicit
      m1: _Task[R],
      m2: _memo[R]
  ): Algebra[Eff[R, ?]] = new Live[R](oneForgeClient)
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

  override def allRate(): Eff[R, Either[ServiceError, List[Rate]]] =
    for {
      result ← fromTask(Task.now(Rate.Pair.allSupported.map(Rate(_, Price(BigDecimal(100)), Timestamp.now))))
    } yield Right(result.toList)


  override def getCached(pair: Rate.Pair): Eff[R, Either[ServiceError, Rate]] = get(pair)
}

private[oneforge] final class Live[R](
    oneForgeClient: OneForgeClient
)(
    implicit
    m1: _Task[R],
    m2: _memo[R]
) extends Algebra[Eff[R, ?]] {


  override def get(
      pair: Rate.Pair
  ): Eff[R, ServiceError Either Rate] = {
    fromTask(Task.fromIO(oneForgeClient.getRate(pair)))
  }

  override def getCached(
    pair: Rate.Pair
  ): Eff[R, ServiceError Either Rate] = {
    taskMemoized("all", allRate()).map { result =>
      result.flatMap { rates =>
        rates.find(_.pair == pair).toRight(Generic(s"Unsupported pair of currency ${pair.show}"))
      }
    }
  }

  override def allRate(): Eff[R, Either[ServiceError, List[Rate]]] =
    fromTask(Task.fromIO(oneForgeClient.getAllRates()))

}
