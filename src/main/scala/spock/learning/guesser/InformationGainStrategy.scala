package spock.learning.guesser

import spock.learning.guesser.distro.PickerDistro
import spock.{Attempt, Range}

class InformationGainStrategy(distro: PickerDistro) extends ChooseStrategy {
  override def choose(attempt: Attempt, range: Range.NonEmpty): Attempt = {
    def informationGain(pivot: Int): Double = {
      val totalInfo = distro.entropy(range)
      val (leftRange, rightRange) = range.splitBy(pivot)
      totalInfo -
        distro.conditional(leftRange, range) * distro.entropy(leftRange) -
          distro.conditional(rightRange, range) * distro.entropy(rightRange)
    }
    val gains = range.iterable.map(pivot => pivot -> informationGain(pivot))
    gains.maxBy(_._2)._1
  }

  override def toString = "information-gain"
}
