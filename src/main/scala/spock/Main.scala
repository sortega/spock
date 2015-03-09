package spock

import spock.learning.guesser._
import spock.learning.picker.LearningPicker

object Main {
  def main(args: Array[String]): Unit = args match {
    case Array("pick") =>
      //LineOrientedIO(new PickerRunner(new NaiveForgetfulPicker))
      LineOrientedIO(new PickerRunner(new LearningPicker))
    case Array("guess") =>
      //LineOrientedIO(new GuesserRunner(new LearningGuesser(FrequencyDistroEstimator.uniformPrior(1))))
      LineOrientedIO(new GuesserRunner(new LearningGuesser(new BayesianDistroEstimator)))
    case unexpected =>
      println("Unexpected arguments: " + unexpected)
      println("usage: java -jar <this jar> (pick | guess)")
  }
}
