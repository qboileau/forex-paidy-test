package forex

package object services {

  type OneForgeAlgebra[F[_]] = oneforge.Algebra[F]
  final val OneForgeInterpreters = oneforge.Interpreters
  type OneForgeError = oneforge.ServiceError
  final val OneForgeError = oneforge.ServiceError

}
