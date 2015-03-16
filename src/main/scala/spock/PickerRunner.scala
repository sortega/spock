package spock

import spock.util.LineOrientedIO

class PickerRunner(var picker: Picker) extends LineOrientedIO.Handler {

  override def onStart(): Seq[String] = {
    Seq(picker.pick.toString)
  }

  override def onLine(line: String): Seq[String] = {
    Picker.Feedback.parse(line).fold(Seq(s"Unexpected input: $line")) { feedback =>
      picker = picker.next(feedback)
      Seq(picker.pick.toString)
    }
  }
}
