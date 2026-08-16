package spock

import spock.Guesser.Feedback
import spock.util.LineOrientedIO

/** Plays the guesser role of the protocol: waits for a `guess` command to start
  * each round, answers greater/lower feedback with further guesses and abandons
  * the round with an `error: ...` line on unexpected input.
  */
class GuesserRunner(private var guesser: Guesser) extends LineOrientedIO.Handler {

  private var inRound = false

  override def onLine(line: String): Seq[String] =
    if (inRound) onFeedback(line) else onCommand(line)

  private def onCommand(line: String): Seq[String] = line match {
    case "guess" =>
      inRound = true
      Seq(guesser.guess.toString)
    case unexpected => error(unexpected)
  }

  private def onFeedback(line: String): Seq[String] = line match {
    case Feedback(feedback @ (Guesser.Greater | Guesser.Lower)) =>
      guesser = guesser.next(feedback)
      Seq(guesser.guess.toString)

    case Feedback(feedback @ (Guesser.Guessed | Guesser.NotGuessed)) =>
      guesser = guesser.next(feedback)
      inRound = false
      Seq.empty

    case unexpected =>
      guesser = guesser.next(Guesser.NotGuessed)
      inRound = false
      error(unexpected)
  }

  private def error(line: String) = Seq(s"error: unexpected input '$line'")
}
