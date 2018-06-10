package forex

package object processes {

  type Rates[F[_]] = rates.Processes[F]
  final val RatesProcesses = rates.Processes
  type RatesError = rates.messages.ProcessError
  final val RatesError = rates.messages.ProcessError

}
