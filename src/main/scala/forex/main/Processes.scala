package forex.main

import forex.config._
import forex.{ services ⇒ s }
import forex.{ processes ⇒ p }
import org.zalando.grafter.macros._

@readerOf[ApplicationConfig]
case class Processes(
  oneForgeConfig: OneForgeConfig,
  actorSystems: ActorSystems,
  executors: Executors
) {
  import actorSystems._

  implicit private val ec =
    executors.default

  implicit final lazy val _oneForge: s.OneForge[AppEffect] =
    s.OneForge.live[AppStack](oneForgeConfig)

  final val Rates = p.Rates[AppEffect]

}
