package forex

package object services {

  type OneForge[F[_]] = oneforge.Algebra[F]
  final val OneForge = oneforge.Interpreters
  type OneForgeError = oneforge.ServiceError
  final val OneForgeError = oneforge.ServiceError

}
