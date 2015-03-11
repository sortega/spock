package spock.learning.guesser.bayesian

import scala.collection.mutable

import spock._
import spock.learning.guesser.distro.PickerDistro

class MarkovChainPickerHypothesis(initialWeight: Double) extends PickerHypothesis {

  private val transitions = {
    val map = mutable.Map.empty[(Int, Int), Double]
    for (source <- MinValue to MaxValue; target <- MinValue to MaxValue) {
      map(source -> target) = initialWeight
    }
    map
  }
  private var lastRange: Range.NonEmpty = Range.Initial
  private var currentDistro: PickerDistro = distroFromSource(lastRange)

  override def distro = currentDistro

  override def probGiven(targetRange: Range.NonEmpty): Double = {
    val prob = currentDistro(targetRange)
    updateWeights(lastRange, targetRange)
    lastRange = targetRange
    currentDistro = distroFromSource(lastRange)
    prob
  }

  private def updateWeights(sourceRange: Range.NonEmpty, targetRange: Range.NonEmpty): Unit = {
    val weight = 1d / (targetRange.size * sourceRange.size)
    for (source <- sourceRange.iterable; target <- targetRange.iterable) {
      transitions.put(source -> target, transitions(source -> target) + weight)
    }
  }

  private def distroFromSource(sourceRange: Range): PickerDistro = {
    val selectedPairs = for {
      ((source, target), weight) <- transitions
      if sourceRange.contains(source)
    } yield target -> weight
    val distro = PickerDistro.normalize(selectedPairs
      .groupBy(_._1)
      .mapValues(pairs => pairs.map(_._2).sum))
    distro
  }

  override def toString = "markov chain"
}
