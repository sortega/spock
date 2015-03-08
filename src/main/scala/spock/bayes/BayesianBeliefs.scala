package spock.bayes

import spock.bayes.BayesianBeliefs.Hypothesis

case class BayesianBeliefs[Observation](hypotheses: Map[Hypothesis[Observation], Double]) {
  require(hypotheses.values.forall(_ >= 0))

  def update(observation: Observation) = BayesianBeliefs(normalize(
    for ((hypothesis, prior) <- hypotheses)
    yield (hypothesis, prior * hypothesis.probGiven(observation))
  ))

  private def normalize(hypotheses: Map[Hypothesis[Observation], Double]) = {
    val totalWeight = hypotheses.values.sum
    if (totalWeight > 0) hypotheses.mapValues(_ / totalWeight)
    else throw new IllegalArgumentException("The impossible has happened")
  }
}

object BayesianBeliefs {
  trait Hypothesis[-Observation] {
    def probGiven(observation: Observation): Double
  }
}
