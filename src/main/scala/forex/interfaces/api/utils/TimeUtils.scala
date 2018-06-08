package forex.interfaces.api.utils

import java.time.{Duration => JDuration}
import scala.concurrent.duration._
import scala.language.implicitConversions

object TimeUtils extends TimeUtils

trait TimeUtils {

  implicit def scalaToJavaDuration(duration: FiniteDuration): JDuration = JDuration.ofNanos(duration.toNanos)
}
