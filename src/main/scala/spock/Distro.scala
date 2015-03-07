package spock

case class Distro(events: Map[Int, Double]) {
  require(events.values.forall(_ >= 0), s"Negative probabilities: $this")
  require(1 - events.values.sum < Eps, s"Probabilities doesn't sum 1: $this")

  def apply(event: Int): Double = events.getOrElse(event, 0d)
  def apply(eventRange: Range): Double = eventRange.iterable.map(apply).sum

  def conditional(event: Int, given: Range): Double =
    if (given.contains(event)) apply(event) / apply(given) else 0

  def conditional(events: Range, given: Range): Double = {
    val hypothesisProb = apply(given)
    if (hypothesisProb == 0) 0
    else {
      val plausibleEvents = events.iterable.toSet intersect given.iterable.toSet
      val eventsProb = plausibleEvents.toSeq.map(apply).sum
      eventsProb / hypothesisProb
    }
  }
}

object Distro {

  def apply(events: (Int, Double)*): Distro = Distro(events.toMap)

  def normalize(events: Map[Int, Double]): Distro = {
    val sum = events.values.sum
    Distro(events.mapValues(_ / sum))
  }

  def uniform(events: Set[Int]): Distro = {
    val weight = 1d / events.size
    Distro(events.map(_ -> weight).toMap)
  }
}
