package spock.learning.picker

trait ExpectedScoresEstimator {
  def learn(observation: GuesserHypothesis.Observation): Unit
  def expectedScores: ExpectedScores
}
