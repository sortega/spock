package spock.forgetful

import spock.Guesser._
import spock.{Guesser, Range}

class NaiveForgetfulGuesser extends Guesser {

  private var range = Range.Initial

  override def guess = (range.upper + range.lower) / 2

  override def notifyFeedback(feedback: Feedback): Unit = {
    range = feedback match {
      case Guessed | NotGuessed => Range.Initial
      case Smaller => Range.NonEmpty(range.lower, guess)
      case Bigger => Range.NonEmpty(guess, range.upper)
    }
  }
}
