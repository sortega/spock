package spock

import spock.Guesser.Feedback

class GuesserRunner(guesser: Guesser) extends LineOrientedIO.Handler {

  override def onStart() = formatGuess

  override def onLine(line: String): Seq[String] = line match {
    case Feedback(feedback) =>
      guesser.notifyFeedback(feedback)
      formatGuess

    case unexpected => Seq(s"Unexpected input: '$unexpected'")
  }

  private def formatGuess = Seq(guesser.guess.toString)
}
