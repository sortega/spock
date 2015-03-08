package spock.forgetful

import scala.util.Random

import spock.Picker.Feedback
import spock._

class NaiveForgetfulPicker extends Picker {
  override def pick: Attempt = 1 + Random.nextInt(100)
  override def notifyFeedback(feedback: Feedback): Unit = {}
}
