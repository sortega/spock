package spock.simple

import spock.Picker.Feedback
import spock._
import spock.util.Choose

class UniformPicker extends Picker {
  override val pick = Choose.randomly(UniformPicker.Values)
  override def next(feedback: Feedback) = new UniformPicker
  override def toString = "uniform picker"
}

object UniformPicker {
  private val Values = (MinValue to MaxValue).toVector
}
