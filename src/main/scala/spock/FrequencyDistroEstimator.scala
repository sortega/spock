package spock

import spock.Range.NonEmpty

class FrequencyDistroEstimator(var frequencies: Map[Int, Double]) extends DistroEstimator {

  override def distro = Distro.normalize(frequencies)

  override def learn(observation: NonEmpty): Unit = {
    val weight = 1d / observation.size
    observation.iterable.foreach { observation =>
      frequencies += observation -> (frequencies(observation) + weight)
    }
  }
}

object FrequencyDistroEstimator {
  def uniformPrior(weight: Double) =
    new FrequencyDistroEstimator((MinValue to MaxValue).zip(Stream.continually(weight)).toMap)
}
