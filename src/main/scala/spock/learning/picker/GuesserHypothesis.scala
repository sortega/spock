package spock.learning.picker

import spock.Picker
import spock.bayes.BayesianBeliefs

trait GuesserHypothesis extends BayesianBeliefs.Hypothesis[GuesserHypothesis.Observation] {
  def expectedScores: ExpectedScores
}

object GuesserHypothesis {
  case class Observation(pick: Int, result: Picker.Feedback)
}
