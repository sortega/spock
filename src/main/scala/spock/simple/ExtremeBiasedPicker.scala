package spock.simple

import spock.Picker.Feedback
import spock._
import spock.util.Choose

class ExtremeBiasedPicker extends Picker {
  override def pick = Choose.randomly(1 to 5)
  override def notifyFeedback(feedback: Feedback): Unit = {}
  override def toString = "biased picker"
}
