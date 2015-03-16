package spock.learning.picker

import spock.Picker
import spock.Picker.Feedback
import spock.learning.picker.GuesserHypothesis.Observation
import spock.util.Choose

class LearningPicker private (estimator: ExpectedScoresEstimator) extends Picker {

  def this() = this(BayesianExpectedScoresEstimator.withDefaultPriors())

  override val pick = Choose.randomlyOverPercentile(0.98, estimator.expectedScores.asMap)

  override def next(feedback: Feedback) =
    new LearningPicker(estimator.learn(Observation(pick, feedback)))

  override def toString = "bayesian picker"
}
