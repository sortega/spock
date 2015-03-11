package spock.learning.guesser.bayesian

import spock._
import spock.learning.guesser.distro.PickerDistro

case object UniformPickerHypothesis extends PickerHypothesis {
  override def probGiven(observation: Range.NonEmpty) = observation.size.toDouble / MaxValue
  override val distro = PickerDistro.uniform((MinValue to MaxValue).toSet)
}

