package spock.learning.guesser.bayesian

import spock._
import spock.bayes.BayesianBeliefs
import spock.learning.guesser.distro.{PickerDistro, DistroEstimator}

class BayesianDistroEstimator(
    beliefs: BayesianBeliefs[Range.NonEmpty, PickerHypothesis] = BayesianBeliefs(Map(
      UniformPickerHypothesis -> 0.5,
      AntiBinaryPickerHypothesis -> 0.3,
      new StaticDistributionHypothesis -> 0.1,
      new MarkovChainPickerHypothesis(0.001) -> 0.1
    ))) extends DistroEstimator {

  override def learn(observation: Range.NonEmpty): BayesianDistroEstimator = {
    new BayesianDistroEstimator(beliefs.update(observation))
  }

  override val distro: PickerDistro = {
    val scaledEvents = for {
      (hypothesis, prob) <- beliefs.hypotheses
    } yield hypothesis.distro.events.mapValues(_ * prob)
    PickerDistro(scaledEvents.flatMap(_.toSeq)
      .groupBy(_._1)
      .mapValues(_.map(_._2).sum))
  }

  override def toString = "bayesian"
}
