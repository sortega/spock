package spock.learning.picker

import spock.Picker
import spock.Picker.{Guessed, NotGuessed}
import spock.learning.picker.GuesserHypothesis.Observation

case object PerfectlyRandomHypothesis extends GuesserHypothesis {
  val scoreProbabilities = Map[Picker.Feedback, Double](
    NotGuessed -> 0.69d,
    Guessed(5) -> 0.16d,
    Guessed(4) -> 0.08d,
    Guessed(3) -> 0.04d,
    Guessed(2) -> 0.02d,
    Guessed(1) -> 0.01d
  )

  val expectedScore =
    scoreProbabilities.map { case (outcome, prob) => outcome.pickerScore * prob }.sum

  override val expectedScores = ExpectedScores(Vector.fill(100)(expectedScore))

  override def probGiven(observation: Observation) = scoreProbabilities(observation.result)
}
