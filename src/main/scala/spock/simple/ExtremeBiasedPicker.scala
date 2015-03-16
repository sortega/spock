package spock.simple

import spock.Picker.Feedback
import spock._
import spock.util.Choose

class ExtremeBiasedPicker extends Picker {
  override val pick = Choose.randomly(1 to 5)
  override def next(feedback: Feedback) = new ExtremeBiasedPicker
  override def toString = "biased picker"
}
