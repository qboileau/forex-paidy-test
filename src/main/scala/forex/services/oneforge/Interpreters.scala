package forex.services.oneforge

import forex.domain._
import monix.eval.Task
import org.atnos.eff._
import org.atnos.eff.addon.monix.task._

object Interpreters {
  def dummy[R](
    implicit
    m1: _task[R]
  ): Algebra[Eff[R, ?]] = new Dummy[R]

  def live[R](
    oneForgeClient: OneForgeClient
  )(
    implicit
    m1: _task[R]
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

}

private[oneforge] final class Live[R](
  oneForgeClient: OneForgeClient
)(
  implicit
  m1: _task[R]
) extends Algebra[Eff[R, ?]] {

  override def get(
    pair: Rate.Pair
  ): Eff[R, ServiceError Either Rate] =
    fromTask(Task.fromIO(oneForgeClient.getRate(pair)))

  override def allRate(): Eff[R, Either[ServiceError, List[Rate]]] =
    fromTask(Task.fromIO(oneForgeClient.getAllRates()))
}