package spock.learning.guesser.bayesian

import spock.Range
import spock.bayes.BayesianBeliefs
import spock.learning.guesser.distro.PickerDistro

trait PickerHypothesis extends BayesianBeliefs.Hypothesis[Range.NonEmpty] {
  def distro: PickerDistro
}

