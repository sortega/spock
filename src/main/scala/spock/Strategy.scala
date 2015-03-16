package spock

import spock.learning.guesser.bayesian.BayesianDistroEstimator
import spock.simple._
import spock.learning.guesser._
import spock.learning.picker.LearningPicker

/** Catalogue of strategies */
object Strategy {
  object Guesser {
    val Naive = new NaiveGuesser
    val RandomBinary = RandomBinaryGuesser()
    val Random31 = Random31Guesser.random()
    val FreqCount = new LearningGuesser(FrequencyDistroEstimator.uniformPrior(0.1), new InformationGainStrategy(_))
    val ExpectedValueBayesian = new LearningGuesser(new BayesianDistroEstimator, new ExpectedValueStrategy(_))
    val InfoGainBayesian = new LearningGuesser(new BayesianDistroEstimator, new InformationGainStrategy(_))
  }
  val Guessers = Seq(Guesser.Naive, Guesser.RandomBinary, Guesser.Random31, Guesser.FreqCount,
    Guesser.ExpectedValueBayesian, Guesser.InfoGainBayesian)
  object Picker {
    val ExtremeBias = new ExtremeBiasedPicker
    val Uniform = new UniformPicker
    val Congruent = new CongruentPicker
    val AntiBinary = new AntiBinaryPicker
    val Bayesian = new LearningPicker
  }
  val Pickers = Seq(Picker.ExtremeBias, Picker.Uniform, Picker.Congruent, Picker.AntiBinary, Picker.Bayesian)
}
