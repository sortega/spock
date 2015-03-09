package spock

import spock.forgetful.NaiveForgetfulPicker
import spock.learning.guesser._

object Main {
  def main(args: Array[String]): Unit = args match {
    case Array("pick") =>
      LineOrientedIO(new PickerRunner(new NaiveForgetfulPicker))
    case Array("guess") =>
      //LineOrientedIO(new GuesserRunner(new LearningGuesser(FrequencyDistroEstimator.uniformPrior(1))))
      LineOrientedIO(new GuesserRunner(new LearningGuesser(new BayesianDistroEstimator)))
    case unexpected =>
      println("Unexpected arguments: " + unexpected)
      println("usage: java -jar <this jar> (pick | guess)")
  }
}
