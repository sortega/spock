package spock.learning.guesser

import spock._
import spock.learning.guesser.distro.PickerDistro
import spock.util.Choose

class InformationGainStrategy(distro: PickerDistro) extends ChooseStrategy {
  override def choose(attempt: Attempt, range: Range.NonEmpty): Attempt = {
    def informationGain(pivot: Int): Double = {
      val totalInfo = distro.entropy(range)
      val (leftRange, rightRange) = range.splitBy(pivot)
      totalInfo -
        distro.conditional(leftRange, range) * distro.entropy(leftRange) -
          distro.conditional(rightRange, range) * distro.entropy(rightRange)
    }
    val gains = range.iterable.map(pivot => pivot -> informationGain(pivot)).toMap
    Choose.randomly(selectBest(gains))
  }

  private def selectBest(weightedValues: Map[Attempt, Double]): Vector[Attempt] = {
    val bestScore = weightedValues.values.filterNot(_.isNaN).reduceOption(_ max _).getOrElse(0d)
    weightedValues.collect {
      case (eligible, score) if score + Eps >= bestScore => eligible
    }.toVector
  }

  override def toString = "information-gain"
}
