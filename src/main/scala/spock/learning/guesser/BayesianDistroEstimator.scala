package spock.learning.guesser

import spock.Range.NonEmpty
import spock._
import spock.bayes.BayesianBeliefs

class BayesianDistroEstimator extends DistroEstimator {

  private trait PickerHypothesis extends BayesianBeliefs.Hypothesis[Range.NonEmpty] {
    def distro: PickerDistro
  }

  private case object UniformPicker extends PickerHypothesis {
    override def probGiven(observation: Range.NonEmpty) = observation.size.toDouble / MaxValue
    override val distro = PickerDistro.uniform((MinValue to MaxValue).toSet)
  }

  private case object AntiBinaryPicker extends PickerHypothesis {
    private val BlackList = Seq(3, 6, 9, 12, 15, 18, 21, 25, 28, 31, 34, 37, 40, 43, 46, 50, 53,
      56, 59, 62, 65, 68, 71, 75, 78, 81, 84, 88, 91, 94, 97)
    override val distro = PickerDistro.uniform((MinValue to MaxValue).toSet -- BlackList)
    override def probGiven(observation: Range.NonEmpty) = distro(observation)
  }

  private case object FrequencyCount extends PickerHypothesis {
    private val count = FrequencyDistroEstimator.uniformPrior(0.01)
    override def distro = count.distro
    override def probGiven(observation: NonEmpty): Double = {
      val prob = distro(observation)
      count.learn(observation)
      prob
    }
  }

  private var beliefs = BayesianBeliefs[Range.NonEmpty, PickerHypothesis](Map(
    UniformPicker -> 0.5,
    AntiBinaryPicker -> 0.4,
    FrequencyCount -> 0.1
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

  override def toString = "bayesian estimation"
}
