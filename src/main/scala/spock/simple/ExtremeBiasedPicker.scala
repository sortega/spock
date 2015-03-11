package spock.simple

import scala.util.Random

import spock.Picker.Feedback
import spock._

class ExtremeBiasedPicker extends Picker {
  override def pick = 1 + Random.nextInt(5)
  override def notifyFeedback(feedback: Feedback): Unit = {}
  override def toString = "naïve uniform picker"
}
