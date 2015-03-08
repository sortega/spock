package spock.learning.guesser

import spock.Guesser._
import spock.{Guesser, Range}

class LearningGuesser(distroEstimator: DistroEstimator) extends Guesser {

  private var strategy = new ExpectedValueStrategy(distroEstimator.distro)
  private var attempt = 1
  private var range = Range.Initial

  override def guess = strategy.choose(attempt, range)

  override def notifyFeedback(feedback: Feedback): Unit = feedback match {
    case Bigger =>
      narrowRange(range.splitBy(guess)._2, feedback)

    case Smaller =>
      narrowRange(range.splitBy(guess)._1, feedback)

    case Guessed =>
      distroEstimator.learn(Range(guess))
      nextRound()

    case NotGuessed =>
      distroEstimator.learn(range)
      nextRound()
  }

  private def narrowRange(newRange: Range, feedback: Feedback): Unit = newRange match {
    case Range.Empty =>
      println(s"Impossible input: cannot be ${feedback.toString.toLowerCase} than $guess " +
        s"when narrowed down to $range at the attempt $attempt")
      nextRound()
    case nonEmptyRange: Range.NonEmpty =>
      attempt += 1
      range = nonEmptyRange
  }

  private def nextRound() = {
    strategy = new ExpectedValueStrategy(distroEstimator.distro)
    attempt = 1
    range = Range.Initial
  }
}
