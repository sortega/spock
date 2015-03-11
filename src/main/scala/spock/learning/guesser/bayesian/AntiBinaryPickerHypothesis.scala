package spock.learning.guesser.bayesian

import spock._
import spock.learning.guesser.distro.PickerDistro

case object AntiBinaryPickerHypothesis extends PickerHypothesis {
  private val BlackList = Seq(3, 6, 9, 12, 15, 18, 21, 25, 28, 31, 34, 37, 40, 43, 46, 50, 53,
    56, 59, 62, 65, 68, 71, 75, 78, 81, 84, 88, 91, 94, 97)
  override val distro = PickerDistro.uniform((MinValue to MaxValue).toSet -- BlackList)
  override def probGiven(observation: Range.NonEmpty) = distro(observation)
}
