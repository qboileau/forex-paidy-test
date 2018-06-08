package forex.main

import forex.config._
import forex.services.oneforge.OneForgeClient
import forex.{ services ⇒ s }
import forex.{ processes ⇒ p }
import org.zalando.grafter.macros._

@readerOf[ApplicationConfig]
case class Processes(
    oneForgeClient: OneForgeClient,
) {

  implicit final lazy val _oneForge: s.OneForgeAlgebra[AppEffect] =
    s.OneForgeInterpreters.live[AppStack](oneForgeClient)

  final val Rates = p.RatesProcesses[AppEffect]

}
