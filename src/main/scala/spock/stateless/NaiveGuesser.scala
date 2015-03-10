package spock.stateless

import spock.Guesser._
import spock.{Guesser, Range}

class NaiveGuesser extends Guesser {

  private var range = Range.Initial

  override def guess = (range.upper + range.lower) / 2

  override def notifyFeedback(feedback: Feedback): Unit = {
    range = feedback match {
      case Guessed | NotGuessed => Range.Initial
      case Smaller => Range.NonEmpty(range.lower, guess - 1)
      case Bigger => Range.NonEmpty(guess + 1, range.upper)
    }
  }

  override def toString = "naïve binary search"
}
