package spock

import spock.stateless.{AntiBinaryPicker, NaiveGuesser, NaivePicker}
import spock.learning.guesser.{BayesianDistroEstimator, FrequencyDistroEstimator, LearningGuesser}
import spock.learning.picker.LearningPicker

/** Catalogue of strategies */
object Strategy {
  object Guesser {
    val Naive = new NaiveGuesser
    val FreqCount = new LearningGuesser(FrequencyDistroEstimator.uniformPrior(0.1))
    val Bayesian = new LearningGuesser(new BayesianDistroEstimator)
  }
  val Guessers = Seq(Guesser.Naive, Guesser.FreqCount, Guesser.Bayesian)
  object Picker {
    val Naive = new NaivePicker
    val AntiBinary = new AntiBinaryPicker
    val Bayesian = new LearningPicker
  }
  val Pickers = Seq(Picker.Naive, Picker.AntiBinary, Picker.Bayesian)
}
