package spock.learning.guesser.distro

import spock._

case class PickerDistro(events: Map[Int, Double]) {
  require(events.keys.forall(e => e >= MinValue && e <= MaxValue), s"Out of range events: $this")
  require(events.values.forall(_ >= 0), s"Negative probabilities: $this")
  require(1 - events.values.sum < Eps, s"Probabilities doesn't sum 1: $this")

  /** 101-element array caching the cumulative distribution functions */
  private lazy val cdf: Array[Double] = (MinValue to MaxValue)
    .scanLeft(0d)((sum, event) => sum + apply(event)).toArray

  def apply(event: Int): Double = events.getOrElse(event, 0d)

  def apply(eventRange: Range): Double = sum(cdf, eventRange)

  def conditional(event: Int, given: Range): Double = conditional(Range(event), given)

  def conditional(events: Range, given: Range): Double = {
    val givenProb = apply(given)
    if (givenProb > 0) apply(events intersect given) / givenProb else 0
  }

  def entropy(range: Range): Double = {
    val givenProb = apply(range)
    (for {
      event <- range.iterable
      rawProb <- events.get(event) if rawProb > 0
      prob = rawProb / givenProb
    } yield -prob * math.log(prob) / PickerDistro.Log2).sum
  }

  private def sum(array: Array[Double], range: Range): Double = range match {
    case Range.Empty => 0d
    case Range.NonEmpty(lower, upper) => array(upper) - array(lower - 1)
  }
}

object PickerDistro {
  private val Log2 = math.log(2)

  def apply(events: (Int, Double)*): PickerDistro = PickerDistro(events.toMap)

  def normalize(events: Map[Int, Double]): PickerDistro = {
    val sum = events.values.sum
    PickerDistro(events.mapValues(_ / sum))
  }

  def uniform(events: Set[Int]): PickerDistro = {
    val weight = 1d / events.size
    PickerDistro(events.map(_ -> weight).toMap)
  }
}
