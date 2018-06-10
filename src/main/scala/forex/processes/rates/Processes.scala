package forex.processes.rates

import cats.Monad
import cats.data.EitherT
import forex.domain._
import forex.services._

object Processes {
  def apply[F[_]]: Processes[F] =
    new Processes[F] {}
}

trait Processes[F[_]] {
  import messages._
  import converters._

  def get(
      request: GetRequest
  )(
      implicit
      M: Monad[F],
      OneForge: OneForgeAlgebra[F]
  ): F[ProcessError Either Rate] = {

    val pair = Rate.Pair(request.from, request.to)
    (for {
      result ← EitherT(OneForge.getCached(pair)).leftMap(toProcessError)
    } yield result).value
  }

  def all()(
      implicit
      M: Monad[F],
      OneForge: OneForgeAlgebra[F]
  ): F[ProcessError Either List[Rate]] =
    (for {
      result ← EitherT(OneForge.allRate()).leftMap(toProcessError)
    } yield result).value

}
