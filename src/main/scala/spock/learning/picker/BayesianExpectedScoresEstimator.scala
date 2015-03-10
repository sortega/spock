package spock.learning.picker

import spock.bayes.BayesianBeliefs
import spock.learning.picker.GuesserHypothesis.Observation

class BayesianExpectedScoresEstimator extends ExpectedScoresEstimator {

  private var beliefs = BayesianBeliefs[GuesserHypothesis.Observation, GuesserHypothesis](Map(
    BinarySearchHypothesis -> 0.5,
    PerfectlyRandomHypothesis -> 0.4,
    new FrequencyCountHypothesis(1) -> 0.1
  ))

  override def learn(observation: Observation): Unit = {
    beliefs = beliefs.update(observation)
  }

  override def expectedScores = ExpectedScores.linearCombination(beliefs.hypotheses.toSeq.collect {
    case (hypothesis, prob) => prob -> hypothesis.expectedScores
  })
}
