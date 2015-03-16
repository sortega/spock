package spock.learning.picker

trait ExpectedScoresEstimator {
  val expectedScores: ExpectedScores
  def learn(observation: GuesserHypothesis.Observation): ExpectedScoresEstimator
}
