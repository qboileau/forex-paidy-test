package forex.processes.rates

import forex.services._

package object converters {
  import messages._

  def toProcessError[T <: Throwable](t: T): ProcessError = t match {
    case OneForgeError.Generic     ⇒ ProcessError.Generic
    case OneForgeError.System(err) ⇒ ProcessError.System(err)
    case e: ProcessError           ⇒ e
    case e                         ⇒ ProcessError.System(e)
  }

}
