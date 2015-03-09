package spock.bayes

import spock.bayes.BayesianBeliefs.Hypothesis

case class BayesianBeliefs[Obs, Hyp <: Hypothesis[Obs]](hypotheses: Map[Hyp, Double]) {
  require(hypotheses.values.forall(_ >= 0))

  def update(observation: Obs): BayesianBeliefs[Obs, Hyp] =
    BayesianBeliefs(normalize(
      for ((hypothesis, prior) <- hypotheses)
      yield (hypothesis, prior * hypothesis.probGiven(observation)),
      observation
    ))

  private def normalize(notNormalized: Map[Hyp, Double], observation: Obs) = {
    val totalWeight = notNormalized.values.sum
    if (totalWeight > 0) notNormalized.mapValues(_ / totalWeight)
    else throw new IllegalArgumentException(s"The impossible has happened: $observation, $hypotheses")
  }
}

object BayesianBeliefs {
  trait Hypothesis[-Observation] {
    def probGiven(observation: Observation): Double
  }
}
