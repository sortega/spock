package spock.learning.picker

import spock.Picker
import spock.Picker.Feedback
import spock.learning.picker.GuesserHypothesis.Observation
import spock.util.Choose

class LearningPicker extends Picker {

  private var estimator = BayesianExpectedScoresEstimator.withDefaultPriors()
  private var currentPick: Int = pickRandomly()

  override def pick = currentPick

  override def notifyFeedback(feedback: Feedback): Unit = {
    estimator = estimator.learn(Observation(currentPick, feedback))
    currentPick = pickRandomly()
  }

  private def pickRandomly(): Int = {
    Choose.randomlyOverPercentile(0.98, estimator.expectedScores.asMap)
  }

  override def toString = "bayesian picker"
}
