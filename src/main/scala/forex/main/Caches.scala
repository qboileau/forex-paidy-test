package forex.main

import cats.Eval
import forex.interfaces.api.utils.TimeUtils._
import com.github.benmanes.caffeine.cache.{ Caffeine, Cache ⇒ CCache }
import forex.config.ApplicationConfig
import org.atnos.eff.Cache
import org.zalando.grafter.macros.readerOf

import scala.concurrent.duration._
import scala.language.implicitConversions

@readerOf[ApplicationConfig]
case class Caches() {

  lazy val underlyingCache: CCache[AnyRef, Eval[Any]] = Caffeine
    .newBuilder()
    .expireAfterWrite(2.minute)
    .build[AnyRef, Eval[Any]]()

  lazy val cache: Cache = new CaffeineCache(underlyingCache)

}

class CaffeineCache(underlyingCache: CCache[AnyRef, Eval[Any]]) extends Cache {

  private lazy val map = underlyingCache.asMap()

  override type C = Cache

  override def memo[V](key: AnyRef, value: ⇒ V): V = {
    lazy val v = value
    if (map.putIfAbsent(key, Eval.later(v).memoize) == null) v
    else map.get(key).asInstanceOf[V]
  }

  override def put[V](key: AnyRef, value: V): V = {
    val v = Eval.now(value)
    map.put(key, v)
    Option(map.get(key)).getOrElse(v).value.asInstanceOf[V]
  }

  override def get[V](key: AnyRef): Option[V] =
    Option(map.get(key)).map(_.value.asInstanceOf[V])

  def reset(key: AnyRef) = {
    map.remove(key)
    this
  }
}
