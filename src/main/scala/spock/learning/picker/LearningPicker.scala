package spock.learning.picker

import spock.Picker
import spock.Picker.Feedback
import spock.learning.picker.GuesserHypothesis.Observation

class LearningPicker extends Picker {

  private val estimator = new BayesianExpectedScoresEstimator
  private var currentPick: Int = pickRandomly()

  override def pick = currentPick

  override def notifyFeedback(feedback: Feedback): Unit = {
    estimator.learn(Observation(currentPick, feedback))
    currentPick = pickRandomly()
  }

  private def pickRandomly(): Int = {
    estimator.expectedScores.chooseAmongBest(0.95)
  }

  override def toString = "bayesian picker"
}
