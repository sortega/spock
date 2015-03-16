package spock.learning.guesser

import spock.Range.NonEmpty
import spock._
import spock.learning.guesser.distro.{DistroEstimator, PickerDistro}

class FrequencyDistroEstimator(frequencies: Map[Int, Double]) extends DistroEstimator {

  override val distro = PickerDistro.normalize(frequencies)

  override def learn(observation: NonEmpty): FrequencyDistroEstimator = {
    val weightIncrease = 1d / observation.size
    new FrequencyDistroEstimator(frequencies = frequencies.map {
      case (value, weight) if observation.contains(value) => (value, weight + weightIncrease)
      case otherwise => otherwise
    })
  }

  override def toString = "frequentist"
}

object FrequencyDistroEstimator {
  def uniformPrior(weight: Double) =
    new FrequencyDistroEstimator((MinValue to MaxValue).zip(Stream.continually(weight)).toMap)
}
