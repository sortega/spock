package spock.learning.guesser.distro

import spock.Range

/** Distribution estimator that learns with each observations */
trait DistroEstimator {
  val distro: PickerDistro

  def learn(observation: Range.NonEmpty): DistroEstimator

  def learn(observations: Seq[Range.NonEmpty]): DistroEstimator =
    observations.foldLeft(this)(_ learn _)
}
