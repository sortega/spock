package spock.stateless

import scala.util.Random

import spock.Picker
import spock.Picker.Feedback

class AntiBinaryPicker extends Picker {
  import AntiBinaryPicker._
  override def pick = SafePicks(Random.nextInt(SafePicks.size))
  override def notifyFeedback(feedback: Feedback): Unit = {}
  override def toString = "anti-binary picker"
}

private object AntiBinaryPicker {
  val BinaryPivots = Set(3, 6, 9, 12, 15, 18, 21, 25, 28, 31, 34, 37, 40, 43, 46, 50, 53, 56, 59,
    62, 65, 68, 71, 75, 78, 81, 84, 88, 91, 94, 97)
  val SafePicks = (1 to 100).filterNot(BinaryPivots).toVector
}
