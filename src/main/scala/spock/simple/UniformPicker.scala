package spock.simple

import spock.Picker.Feedback
import spock._
import spock.util.Choose

class UniformPicker extends Picker {
  private val values = (MinValue to MaxValue).toVector
  override def pick = Choose.randomly(values)
  override def notifyFeedback(feedback: Feedback): Unit = {}
  override def toString = "uniform picker"
}
