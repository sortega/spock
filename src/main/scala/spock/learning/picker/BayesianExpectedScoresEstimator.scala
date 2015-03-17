package spock.learning.picker

import spock.bayes.BayesianBeliefs
import spock.learning.picker.GuesserHypothesis.Observation

case class BayesianExpectedScoresEstimator(beliefs: BayesianExpectedScoresEstimator.Beliefs)
  extends ExpectedScoresEstimator {

  override val expectedScores = ExpectedScores.linearCombination(beliefs.hypotheses.toSeq.collect {
    case (hypothesis, prob) => prob -> hypothesis.expectedScores
  })

  override def learn(observation: Observation) = copy(beliefs.update(observation))
}

object BayesianExpectedScoresEstimator {
  type Beliefs = BayesianBeliefs[GuesserHypothesis.Observation, GuesserHypothesis]

  def withDefaultPriors() = BayesianExpectedScoresEstimator(BayesianBeliefs(Map(
    BinarySearchHypothesis -> 0.5,
    PerfectlyRandomHypothesis -> 0.3,
    new FrequencyCountHypothesis(1) -> 0.1,
    ProfiledGuesserHypothesis.RandomBinary -> 0.05,
    ProfiledGuesserHypothesis.Random31 -> 0.05
  )))
}
