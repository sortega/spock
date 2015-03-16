package spock.simple

import spock._
import spock.util.Choose

case class RandomBinaryGuesser(range: Range.NonEmpty, override val guess: Int) extends Guesser {

  override def next(feedback: Guesser.Feedback): Guesser = feedback match {
    case Guesser.Smaller => RandomBinaryGuesser(Range.NonEmpty(range.lower, guess - 1))
    case Guesser.Bigger => RandomBinaryGuesser(Range.NonEmpty(guess + 1, range.upper))
    case _ => RandomBinaryGuesser()
  }

  override def toString = "random binary guesser"
}

object RandomBinaryGuesser {
  def apply(range: Range.NonEmpty = Range.Initial): RandomBinaryGuesser =
    RandomBinaryGuesser(range, Choose.randomly(range.iterable.toSeq))
}
