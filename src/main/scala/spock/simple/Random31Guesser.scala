package spock.simple

import spock.Guesser.{Bigger, Feedback, Smaller}
import spock._
import spock.util.Choose

class Random31Guesser(chosenSubset: Seq[Int]) extends Guesser {
  require(chosenSubset.nonEmpty)

  override val guess = chosenSubset.sorted.apply(chosenSubset.size / 2)

  override def next(feedback: Feedback): Random31Guesser = feedback match {
    case Smaller => new Random31Guesser(chosenSubset.filter(_ < guess))
    case Bigger => new Random31Guesser(chosenSubset.filter(_ > guess))
    case _ => Random31Guesser.random()
  }

  override def toString: String = "random-31 binary guesser"
}

object Random31Guesser {
  def random(): Random31Guesser = new Random31Guesser(choose31())

  private def choose31(): Seq[Int] =
    Choose.Random.shuffle(MinValue to MaxValue).take(31).toSeq
}
