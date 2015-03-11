package spock.learning.guesser.bayesian

import spock._
import spock.bayes.BayesianBeliefs
import spock.learning.guesser.distro.{PickerDistro, DistroEstimator}

class BayesianDistroEstimator extends DistroEstimator {

  private var beliefs = BayesianBeliefs[Range.NonEmpty, PickerHypothesis](Map(
    UniformPickerHypothesis -> 0.5,
    AntiBinaryPickerHypothesis -> 0.3,
    new StaticDistributionHypothesis -> 0.1,
    new MarkovChainPickerHypothesis(0.001) -> 0.1
  ))

  override def learn(observation: Range.NonEmpty): Unit = {
    beliefs = beliefs.update(observation)
  }

  override def distro: PickerDistro = {
    val scaledEvents = for {
      (hypothesis, prob) <- beliefs.hypotheses
    } yield hypothesis.distro.events.mapValues(_ * prob)
    PickerDistro(scaledEvents.flatMap(_.toSeq)
      .groupBy(_._1)
      .mapValues(_.map(_._2).sum))
  }

  override def toString = "bayesian"
}
