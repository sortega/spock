package spock.learning.guesser.bayesian

import spock.Range.NonEmpty
import spock.learning.guesser.FrequencyDistroEstimator

class StaticDistributionHypothesis extends PickerHypothesis {
  private val count = FrequencyDistroEstimator.uniformPrior(0.01)

  override def distro = count.distro

  override def probGiven(observation: NonEmpty): Double = {
    val prob = distro(observation)
    count.learn(observation)
    prob
  }

  override def toString = "static distribution"
}
