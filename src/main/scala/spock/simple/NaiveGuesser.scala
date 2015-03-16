package spock.simple

import spock.Guesser._
import spock.{Guesser, Range}

case class NaiveGuesser(range: Range.NonEmpty = Range.Initial) extends Guesser {

  override val guess = (range.upper + range.lower) / 2

  override def next(feedback: Feedback): NaiveGuesser = feedback match {
    case Guessed | NotGuessed => NaiveGuesser()
    case Smaller => NaiveGuesser(Range.NonEmpty(range.lower, guess - 1))
    case Bigger => NaiveGuesser(Range.NonEmpty(guess + 1, range.upper))
  }

  override def toString = "naïve binary search"
}
