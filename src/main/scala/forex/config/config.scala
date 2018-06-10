package forex.config

import org.zalando.grafter.macros._
import pureconfig.ConfigReader

import scala.concurrent.duration.FiniteDuration

@readers
case class ApplicationConfig(
    akka: AkkaConfig,
    api: ApiConfig,
    executors: ExecutorsConfig,
    oneforge: OneForgeConfig
)

case class AkkaConfig(
    name: String,
    exitJvmTimeout: Option[FiniteDuration]
)

case class ApiConfig(
    interface: String,
    port: Int
)

case class ExecutorsConfig(
    default: String
)

case class ApiKey(value: String) extends AnyVal
case class OneForgeConfig(
    apiKey: ApiKey,
    baseUrl: String
)

object ApiKey {
  implicit val apiKeyReader: ConfigReader[ApiKey] =
    ConfigReader.fromNonEmptyString[ApiKey](str => _ => Right(ApiKey(str)))
}

