package forex.processes.rates

import forex.domain._
import scala.util.control.NoStackTrace

package messages {

  sealed trait ProcessError extends Throwable with NoStackTrace
  object ProcessError {
    final case object Generic extends ProcessError
    final case class Parsing(msg: String) extends ProcessError
    final case class System(underlying: Throwable) extends ProcessError
  }

  final case class GetRequest(
      from: Currency,
      to: Currency
  )
}
