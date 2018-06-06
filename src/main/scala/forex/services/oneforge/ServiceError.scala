package forex.services.oneforge

import scala.util.control.NoStackTrace

sealed trait ServiceError extends Throwable with NoStackTrace
object ServiceError {
  final case object Generic extends ServiceError
  final case class System(underlying: Throwable) extends ServiceError
}