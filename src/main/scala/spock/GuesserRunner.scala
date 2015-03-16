package spock

import spock.Guesser.Feedback
import spock.util.LineOrientedIO

class GuesserRunner(private var guesser: Guesser) extends LineOrientedIO.Handler {

  override def onStart() = formatGuess

  override def onLine(line: String): Seq[String] = line match {
    case Feedback(feedback) =>
      guesser = guesser.next(feedback)
      formatGuess

    case unexpected => Seq(s"Unexpected input: '$unexpected'")
  }

  private def formatGuess = Seq(guesser.guess.toString)
}
