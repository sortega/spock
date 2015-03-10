package spock.stateless

import scala.util.Random

import spock.Picker.Feedback
import spock._

class NaivePicker extends Picker {
  override def pick = 1 + Random.nextInt(100)
  override def notifyFeedback(feedback: Feedback): Unit = {}
  override def toString = "naïve uniform picker"
}
