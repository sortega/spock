package spock

import spock.util.LineOrientedIO

/** Plays the picker role of the protocol: waits for a `pick` command to start
  * each round, then for the round result (`guessed N` or `not-guessed`) and
  * abandons the round with an `error: ...` line on unexpected input.
  */
class PickerRunner(private var picker: Picker) extends LineOrientedIO.Handler {

  private var awaitingResult = false

  override def onLine(line: String): Seq[String] =
    if (awaitingResult) onResult(line) else onCommand(line)

  private def onCommand(line: String): Seq[String] = line match {
    case "pick" =>
      awaitingResult = true
      Seq(picker.pick.toString)
    case unexpected => error(unexpected)
  }

  private def onResult(line: String): Seq[String] = {
    awaitingResult = false
    Picker.Feedback.parse(line) match {
      case Some(feedback) =>
        picker = picker.next(feedback)
        Seq.empty
      case None =>
        picker = picker.next(Picker.NotGuessed)
        error(line)
    }
  }

  private def error(line: String) = Seq(s"error: unexpected input '$line'")
}
