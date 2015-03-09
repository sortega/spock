package spock.learning.picker

import spock._
import spock.learning.picker.GuesserHypothesis.Observation

class FrequencyCountHypothesis(initialWeight: Double) extends GuesserHypothesis {

  private val counts = Vector.fill(MaxValue)(new AttemptsCount(initialWeight))

  override def expectedScores = ExpectedScores(counts.map(_.expectedScore))

  override def probGiven(observation: Observation) = {
    val relevantCount = counts(observation.pick - 1)
    val prob = relevantCount.probGiven(observation.result)
    relevantCount.count(observation.result)
    prob
  }
}
