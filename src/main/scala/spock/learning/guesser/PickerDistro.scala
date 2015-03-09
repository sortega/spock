package spock.learning.guesser

import spock._

case class PickerDistro(events: Map[Int, Double]) {
  require(events.keys.forall(e => e >= MinValue && e <= MaxValue), s"Out of range events: $this")
  require(events.values.forall(_ >= 0), s"Negative probabilities: $this")
  require(1 - events.values.sum < Eps, s"Probabilities doesn't sum 1: $this")

  /** 101-element array caching the cumulative distribution functions */
  private lazy val cdf: Array[Double] = (MinValue to MaxValue)
    .scanLeft(0d)((sum, event) => sum + apply(event)).toArray

  def apply(event: Int): Double = events.getOrElse(event, 0d)

  def apply(eventRange: Range): Double = eventRange match {
    case Range.Empty => 0
    case Range.NonEmpty(lower, upper) => cdf(upper min MaxValue) - cdf((lower max MinValue) - 1)
  }

  def conditional(event: Int, given: Range): Double = conditional(Range(event), given)

  def conditional(events: Range, given: Range): Double = {
    val givenProb = apply(given)
    if (givenProb > 0) apply(events intersect given) / givenProb else 0
  }
}

object PickerDistro {

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
